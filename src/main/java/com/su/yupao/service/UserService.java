package com.su.yupao.service;

import com.su.yupao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author suhon
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-09-09 19:59:30
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册校验
     * @param account 账户名
     * @param password 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    long Register(String account,String password,String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param account 账户名
     * @param password 密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String account, String password, HttpServletRequest request);

    User getSafetyUser(User orignUser);

    int logout(HttpServletRequest request);

    List<User> selectUserByTags(List<String> tagNameList);

    int updateUser(User user, User loginUser);
    boolean isAdmin(HttpServletRequest httpServletRequest);
    boolean isAdmin(User user);

    User getLoginUser(HttpServletRequest request);
}
