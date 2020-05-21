package com.robot2.secret.entity;

import lombok.Data;

@Data
public class BaseInfo {
    private int id;              //info表id
    private int secretId;        //secret表id
    private String deviceId;     //info表deviceId
    private String username;     //用户注册username
    private String password;     //用户注册password
    private String name;         //机器人、平台用户名
    private String phone;        //电话
    private String email;        //邮箱
    private String address;      //地址
    private String org1;         //公司一级名称
    private String org2;         //公司二级名称
    private String identity;     //用户identity
    private String type;         //类型，用户/机器人/平台
    private int online;          //上线、下线


}
