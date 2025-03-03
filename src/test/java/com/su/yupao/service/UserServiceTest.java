package com.su.yupao.service;

import java.util.Arrays;
import java.util.List;

import com.su.yupao.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("ming");
        user.setUserAccount("2122");
        user.setAvatarUrl("\"D:\\img\\school.jpg\"");
        user.setGender(0);
        user.setUserPassword("111222");
        user.setPhone("111222");
        user.setEmail("111222");
        boolean result = userService.save(user);
        System.out.println(user.getId());//mybatis-plus主键自动回填
        Assertions.assertEquals(true, result);


    }

    @Test
    void register() {
        //测试非空
        String userAccount = "suhongrun";
        String userPassword = "";
        String checkPassword = "12345678";
        String planetCode = "223";
        long result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        //测试账号长度不小于4
        userAccount = "su";
        userPassword = "12345678";
        result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        //测试用户密码不小于8
        userAccount = "suhongrun";
        userPassword = "1234";
        checkPassword = "1234";
        result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        //测试账户不能重复
        userAccount = "susu1";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertTrue(result > 0);
        userAccount = "susu1";
        result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        //测试账户不能包含特殊字符
        userAccount = "suhong run";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.Register(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        //测试密码相同
        userAccount = "suhongrun";
        userPassword = "1234222211";
        checkPassword = "12345678";
        result = userService.Register(userAccount, userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1, result);

    }
    @Test
    void selectUserByTags(){
        List<String> list = Arrays.asList("java", "python");
        List<User> users = userService.selectUserByTags(list);
        Assert.notNull(users);

    }
}