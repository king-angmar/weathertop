package xyz.wongs.weathertop.comp;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockComponent {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * @Description 获取锁，默认：失效时间为5，失效时间的单位为秒，重试次数为3，休眠6秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value){
        return getLock(key,value,5, TimeUnit.SECONDS,3,5000);
    }

    /**
     * @Description 获取锁，默认：失效时间的单位为秒，重试次数为3，休眠6秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire){
        return getLock(key,value,expire, TimeUnit.SECONDS,3,5000);
    }

    /**
     * @Description 获取锁，默认重试次数为3，休眠6秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @param unit
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire, TimeUnit unit){
        return getLock(key,value,expire, unit,3,5000);
    }

    /**
     * @Description
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @param unit
     * @param tryCount  重试次数
     * @param waitMillis 等待时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire, TimeUnit unit,int tryCount,int waitMillis){
        //setIfAbsent如果键不存在则新增,存在则不改变已经有的值。
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key,value,expire,unit);
        if(success) {
            return true;
        }
        //判断锁超时 - 防止原来的操作异常，没有运行解锁操作  防止死锁
        String val = stringRedisTemplate.opsForValue().get(key);
        if(!Strings.isNullOrEmpty(val)){
            if(System.currentTimeMillis() - Long.parseLong(val) > unit.toMillis(expire)){
                // 超时移除
                stringRedisTemplate.delete(key);
            }
        }
        // 重试、等待
        if (tryCount > 0 && waitMillis > 0) {
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                log.error("getLock exception{}",e.getMessage());
            }
            return getLock(key,value,expire,unit,tryCount - 1,waitMillis);
        }
        return false;
    }

    /**
     * @Description 获取等待时间
     * @param key
     * @param expire
     * @param unit
     * @return long 秒
     * @throws
     * @date 2019/11/16 21:44
     */
    public long getWaitSecond(String key,long expire,TimeUnit unit) {
        long currentTime = System.currentTimeMillis();
        long preTime = Long.parseLong(stringRedisTemplate.opsForValue().get(key));
        return (preTime + unit.toMillis(expire) - currentTime) / 1000;
    }


    /**
     * @Description 设置锁的过期时间，默认单位为毫秒
     * @param key
     * @param expTime
     * @return Boolean
     * @throws
     * @date 2019/11/16 21:18
     */
    public Boolean renewal(String key,int expTime){
        return renewal(key, expTime, TimeUnit.MILLISECONDS);
    }

    /**
     * @Description 设置锁的过期时间
     * @param key
     * @param expTime
     * @param unit
     * @return Boolean
     * @throws
     * @date 2019/11/16 21:18
     */
    public Boolean renewal(String key,int expTime,TimeUnit unit){
        return stringRedisTemplate.expire(key, expTime, unit);
    }


    /**
     * @Description 解锁
     * @param key
     * @param val
     * @return void
     * @throws
     * @date 2019/11/16 21:13
     */
    public void unlock(String key,String val){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            if(!Strings.isNullOrEmpty(value) && val.equals(value) ){
                // 删除锁状态
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("unlock exception{}",e);
        }
    }

}
