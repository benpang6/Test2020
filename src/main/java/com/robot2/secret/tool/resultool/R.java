package com.robot2.secret.tool.resultool;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {
    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    // 通用返回成功
    public static R ok() {
        R r = new R();
        r.setSuccess(Renum.SUCCESS.getSuccess());
        r.setCode(Renum.SUCCESS.getCode());
        r.setMessage(Renum.SUCCESS.getMessage());
        return r;
    }

    // 通用返回失败，未知错误
    public static R error() {
        R r = new R();
        r.setSuccess(Renum.UNKNOWN_ERROR.getSuccess());
        r.setCode(Renum.UNKNOWN_ERROR.getCode());
        r.setMessage(Renum.UNKNOWN_ERROR.getMessage());
        return r;
    }
    // 设置结果，形参为结果枚举
    public static R setResult(Renum result) {
        R r = new R();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }
    // 通用设置data
    public R data(Object value) {
        this.data.put("result", value);
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }
}
