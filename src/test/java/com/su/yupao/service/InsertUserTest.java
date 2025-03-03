package com.su.yupao.service;

import com.su.yupao.mapper.UserMapper;
import com.su.yupao.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class InsertUserTest {
    @Resource
    UserMapper userMapper;
    @Resource
    UserService userService;

    /**
     * 并发插入用户
     */
    @Test
    public void doInsert(){
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        final int NUM = 100000;
        int j = 0;
        //并发任务集合
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<User> userList = new ArrayList<>();
            while (true){
                j++;
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
                userList.add(user);
                if(j % 10000 == 0){
                    break;
                }
            }
            //异步执行任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, 10000);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopwatch.stop();
        System.out.println(stopwatch.getTotalTimeMillis());
    }

}
