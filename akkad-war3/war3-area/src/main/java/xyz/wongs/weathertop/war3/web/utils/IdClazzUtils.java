package xyz.wongs.weathertop.war3.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.base.persistence.mybatis.service.RedisUidService;

@Component
public class IdClazzUtils {

    @Autowired
    private RedisUidService redisUidService;

    public Long getId(Class<?> clazz){
        return Long.valueOf(redisUidService.generate(clazz.getSimpleName().toUpperCase()));
    }

}
