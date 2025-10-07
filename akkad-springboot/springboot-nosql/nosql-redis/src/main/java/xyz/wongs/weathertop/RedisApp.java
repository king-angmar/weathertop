package xyz.wongs.weathertop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.*;
import org.springframework.integration.redis.util.RedisLockRegistry;
import xyz.wongs.weathertop.nosql.redis.service.RedisLockService;
import xyz.wongs.weathertop.nosql.redis.service.RedisOptService;

@SpringBootApplication
public class RedisApp {
    public static void main(String[] args) {
        SpringApplication.run(RedisApp.class,args);
    }


    @Bean
    public RedisOptService redisService(RedisTemplate<String, Object> redisTemplate,
                                        ValueOperations<String, Object> valueOperations,
                                        HashOperations<String, String, Object> hashOperations,
                                        SetOperations<String, Object> setOperations,
                                        ListOperations<String, Object> listOperations,
                                        ZSetOperations<String, Object> zSetOperations) {
        return new RedisOptService(redisTemplate, valueOperations,
                hashOperations, listOperations, setOperations, zSetOperations);
    }

    @Bean
    public RedisLockService redisLockService(RedisLockRegistry redisLockRegistry) {
        return new RedisLockService(redisLockRegistry);
    }
}
