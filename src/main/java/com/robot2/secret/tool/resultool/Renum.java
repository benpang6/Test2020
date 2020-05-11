package com.robot2.secret.tool.resultool;

import lombok.Getter;

@Getter
public enum Renum {
    SUCCESS(true,200,"成功"),
    UNKNOWN_ERROR(false,101,"未知错误"),
    PARAM_ERROR(false,102,"参数错误"),
    NULL_POINT(false,103,"空指针异常"),
    HTTP_CLIENT_ERROR(false,104,"HTTP客户端错误"),
    INTERFACE_NOT_AVALIABLE_NUMBER(false,105,"证书不可用。接口调用次数已用尽，请重新购买"),
    INTERFACE_NOT_AVALIABLE_TIME(false,106,"证书已过期，暂不可用，请尽快续费"),
    AUTHORITY_FAILE(false,107,"非法秘钥，权限认证失败"),
    REGISTER_FAILE(false,108,"已通过注册认证，请勿重复认证"),
    INTERFACE_NOT_NUMBERS_FAILE(false,109,"查询接口剩余调用次数失败");

    //响应是否成功
    private final Boolean success;
    //响应状态码
    private final Integer code;
    //响应信息
    private final String message;

    Renum(Boolean success, int code, String messsage) {
        this.success=success;
        this.code=code;
        this.message=messsage;
    }
}
