package com.su.yupao.service;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.RedissonLiveObjectService;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissionTest {
    @Resource
    private RedissonClient redissonClient;
    @Test
    public void test(){
        RList<Object> rList = redissonClient.getList("test_su");
        //rList.add("yupao");
        System.out.println("rlist："+rList.get(0));
        rList.remove(0);
    }
}
