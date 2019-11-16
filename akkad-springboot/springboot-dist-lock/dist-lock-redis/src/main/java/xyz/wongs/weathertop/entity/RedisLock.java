package xyz.wongs.weathertop.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.wongs.weathertop.base.utils.StringUtils;

import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class RedisLock {

    private RedisTemplate<String, String> redisTemplate;

    /**
     * redis.key
     */
    private String key;

    /**
     * redis.value
     */
    private String value;

    /**
     * redis过期时间
     */
    private int expire = 60000;

    /**
     * 等待时间
     */
    private int waitMillis = 500;

    /**
     * 重试次数
     */
    private int tryCount = 3;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public RedisLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String key, int expire) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expire = expire;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String key, int expire, int waitMillis, int tryCount) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expire = expire;
        this.waitMillis = waitMillis;
        this.tryCount = tryCount;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, String key,
                     int expire, TimeUnit timeUnit, int waitMillis, int tryCount) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expire = expire;
        this.waitMillis = waitMillis;
        this.tryCount = tryCount;
        this.timeUnit = timeUnit;
    }

    public boolean getLock() {
        try {
            return getLock(tryCount);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取锁
     *
     * @param remainTryCount 重试次数
     * @return true if success
     * @throws Exception
     */
    private boolean getLock(int remainTryCount) throws Exception {
        this.value = System.currentTimeMillis() + "";
        // 如果成功 设置这个key的过期时间
        // setIfAbsent(K var1, V var2, long var3, TimeUnit var5)方法在spring-data-redis 2.x中支持
        boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
        if (success) {
            return true;
        } else {
            // 失败  获取值 判断key是否超时未移除
            String value = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotEmpty(value)) {
                if (System.currentTimeMillis() - Long.parseLong(value) > timeUnit.toMillis(expire)) {
                    // 超时移除
                    redisTemplate.delete(key);
                }
            }
            // 重试、等待
            if (remainTryCount > 0 && waitMillis > 0) {
                Thread.sleep(waitMillis);
                return getLock(remainTryCount - 1);
            } else {
                return false;
            }
        }
    }

    /**
     * 获取等待时间
     *
     * @return 等待时间
     */
    public long getWaitSecond() {
        long currentTime = System.currentTimeMillis();
        long preTime = Long.parseLong(redisTemplate.opsForValue().get(key));
        return (preTime + timeUnit.toMillis(expire) - currentTime) / 1000;
    }

    /**
     * 释放锁
     */
    public void release() {
        if (value == null || key == null) {
            return;
        }
        if (value.equals(redisTemplate.opsForValue().get(key))) {
            redisTemplate.delete(key);
        }
    }
}
