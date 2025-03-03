package com.su.yupao.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.su.yupao.model.domain.User;
import com.su.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    //设置核心用户，预热时加载核心用户
    List<Long> mainUser = Arrays.asList(1L);

    @Scheduled(cron = "0 8 0 * * *")
    public void doCacheRecommendUser() {
        for (Long userId : mainUser) {
            //查数据库
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            //redis的查询key
            String redisKey = String.format("yupao:user:recommend:%s", userId);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            //写缓存
            try {
                valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("redis set key error", e);
            }
        }

    }
}
