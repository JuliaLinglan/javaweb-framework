package com.linglan.smart.model;

import lombok.Data;

@Data
public class Customer {
    private long id;
    private String name;
    private String contact;
    private String phone;
    private String email;
    private String remark;
}
