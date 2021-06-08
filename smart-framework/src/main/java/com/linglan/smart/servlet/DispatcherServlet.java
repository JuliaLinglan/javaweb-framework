package com.linglan.smart.servlet;

import com.linglan.smart.config.ConfigHelper;
import com.linglan.smart.init.HelperLoader;
import com.linglan.smart.ioc.BeanHelper;
import com.linglan.smart.ioc.ReflectionUtil;
import com.linglan.smart.request.ControllerHelper;
import com.linglan.smart.request.Handler;
import com.linglan.smart.request.Param;
import com.linglan.smart.response.Data;
import com.linglan.smart.response.View;
import com.linglan.smart.utils.ArrayUtil;
import com.linglan.smart.utils.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    public void init(ServletConfig servletConfig) {
        HelperLoader.init();

        //获取servletContext对象（用于注册servlet）
        ServletContext servletContext = servletConfig.getServletContext();

        //注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        //注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }


    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //获取请求方法与请求路径
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();

        //获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            //获取Controller类及Bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //创建请求参数对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtil.isNotEmpty(body)) {
                String[] params = StringUtil.splitString(body, "&");
                if (ArrayUtil.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = StringUtil.splitString(param, "=");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);
            //调用action方法
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);

            //处理action方法返回值
            if (result instanceof View) {
                //返回jsp页面
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)) {
                    response.sendRedirect(request.getContextPath() + path);
                } else {
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
                }
            } else if (result instanceof Data) {
                //返回json数据
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
