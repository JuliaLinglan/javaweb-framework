package com.linglan.smart.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * pojo转json
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJson(T obj) {
        String json;

        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error("cover POJO to JSON fail", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * json转pojo
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T pojo;

        try {
            pojo = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            LOGGER.error("cover JSON to POJO fail", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }


}
