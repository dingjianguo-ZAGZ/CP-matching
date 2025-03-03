package com.su.yupao.model.request;

import lombok.Data;


import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 7511656387857061844L;
    private String account;
    private String password;
    private String checkPassword;
    private String planetCode;
}
