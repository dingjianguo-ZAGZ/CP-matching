package com.su.yupao.once;

import com.su.yupao.mapper.UserMapper;
import com.su.yupao.model.domain.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InsertUser {
    @Resource
    UserMapper userMapper;

    /**
     * 批量插入用户
     */
    public void doInsert(){
        final int NUM = 10000000;
        for (int i = 0; i < NUM; i++) {
            User user = new User();
            user.setUsername("苏妲己");
            user.setUserAccount("0606");
            user.setAvatarUrl("src/img/nazha.jpg");
            user.setGender(1);
            user.setUserPassword("12345678");
            user.setTags("[]");
            user.setPhone("15245789999");
            user.setEmail("1111qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("22334");

            userMapper.insert(user);
        }
    }

}
