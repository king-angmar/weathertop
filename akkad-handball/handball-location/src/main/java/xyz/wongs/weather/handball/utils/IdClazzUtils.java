package xyz.wongs.weather.handball.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.wongs.weather.base.persistence.mybatis.service.RedisUidService;
import xyz.wongs.weather.handball.location.entity.Location;

@Component
public class IdClazzUtils {

    @Autowired
    private RedisUidService redisUidService;

    public Long getId(){
        return Long.valueOf(redisUidService.generate(Location.class.getSimpleName().toUpperCase()));
    }
}
