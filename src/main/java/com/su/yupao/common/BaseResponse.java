package com.su.yupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code, T data,String description) {
        this(code,data,"",description);
    }
    public BaseResponse(int code, String message,String description) {
        this(code,null,message,description);
    }





    public BaseResponse(T data, int code) {
        this(code,data,"","");
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMessage(),errorCode.getDescription());
    }

}
