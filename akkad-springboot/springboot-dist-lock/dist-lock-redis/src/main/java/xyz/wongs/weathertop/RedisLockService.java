package xyz.wongs.weathertop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.entity.RedisLock;

@Service
@Slf4j
public class RedisLockService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public T methodCache(){
        RedisLock redisLock =new RedisLock(redisTemplate, key, 60000, 3000, 2);
        // 请求锁
        boolean locked = redisLock.getLock();
        if (!locked) {
            throw new Exception("获取redis锁失败！");
        }
        try {
            // doSomething
            // insertInto tb_xx
            // update tb_xx
        } catch (Exception e) {
            log.error("methodCache...error:{}", e);
            // 异常被捕获，如果需要回滚事物，一定要抛出异常
            throw e;
        } finally {
            // 释放锁
            redisLock.release();
        }
    }
}
