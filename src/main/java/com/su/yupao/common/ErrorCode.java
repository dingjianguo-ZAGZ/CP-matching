package com.su.yupao.common;

/**
 * 错误码
 * @author su
 */
public enum ErrorCode {
    SUCCESS(0, "OK", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000,"系统内部异常","")
    ;
    private int code;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 错误描述
     */
    private String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public String getDescription() {
        return description;
    }

}
