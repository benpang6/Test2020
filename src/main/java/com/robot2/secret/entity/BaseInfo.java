package com.robot2.secret.entity;

import lombok.Data;

@Data
public class BaseInfo {
    private int id;
    private int secretId;
    private String deviceId;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String org1;
    private String org2;
    private String identity;
    private String type;
    private int online;


}
