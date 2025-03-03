package com.su.yupao.service;

import com.su.yupao.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    public RedisTemplate redisTemplate;
    @Test
    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //增
        valueOperations.set("string","susu");
        valueOperations.set("int",1);
        valueOperations.set("double",1.0);
        User user = new User();
        user.setId(1L);
        user.setGender(0);
        valueOperations.set("object",user);
        //查
        Object susu = (String) valueOperations.get("string");
        Assertions.assertTrue("susu".equals(susu));
        susu = valueOperations.get("int");
        Assertions.assertTrue(1 == (Integer) susu);
        susu = valueOperations.get("double");
        Assertions.assertTrue(susu.equals(1.0));
        System.out.println(valueOperations.get("object"));
    }
}
