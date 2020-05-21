package com.robot2.secret.tool.resultool;

import lombok.Getter;

@Getter
public enum Renum {
    SUCCESS(true,200,"成功"),
    UNKNOWN_ERROR(false,1001,"未知错误"),
    PARAM_ERROR(false,1002,"参数错误"),
    NULL_POINT(false,1003,"空指针异常"),
    HTTP_CLIENT_ERROR(false,1004,"HTTP客户端错误"),
    INTERFACE_NOT_AVALIABLE_NUMBER(false,1005,"证书不可用。接口调用次数已用尽，请重新购买"),
    INTERFACE_NOT_AVALIABLE_TIME(false,1006,"证书已过期，暂不可用，请尽快续费"),
    AUTHORITY_FAILE(false,1007,"非法秘钥，权限认证失败"),
    REGISTER_FAILE(false,1008,"已通过注册认证，请勿重复认证"),
    INTERFACE_NOT_NUMBERS_FAILE(false,1009,"查询接口剩余调用次数失败"),
    DELETE_SUCCESS(true,200,"删除成功"),  //机器人/平台删除成功
    DELETE_NOT_AVALIABLE_NUMBER(false,2001,"没有需要删除的数据"),  //机器人/平台删除失败
    DELETE_NOT_CHOICE(false,2002,"请选择删除的内容");//

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
