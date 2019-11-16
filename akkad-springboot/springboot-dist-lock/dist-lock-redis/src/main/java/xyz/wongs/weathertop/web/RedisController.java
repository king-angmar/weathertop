package xyz.wongs.weathertop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.comp.RedisLockComponent;
import xyz.wongs.weathertop.deno.entity.RedisLock;
import xyz.wongs.weathertop.deno.mapper.RedisLockMapper;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class RedisController {

    @Autowired
    private RedisLockMapper redisLockMapper;

    @Autowired
    private RedisLockComponent redisLockComponent;

    /**
     * 超时时间 5s
     */
    private static final int TIMEOUT = 3;

    @RequestMapping(value = "/seckilling/{key}")
    public ResponseResult Seckilling(@PathVariable("key") String key){
        ResponseResult responseResult = new ResponseResult();
        //1、加锁
        String value = System.currentTimeMillis() + "";
        if(!redisLockComponent.getLock(key,value,TIMEOUT, TimeUnit.SECONDS,3,6000)){
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.DICT_LOCK_FAIL.getCode());
            responseResult.setMsg("排队人数太多，请稍后再试.");
            return responseResult;
        }

        RedisLock redisLock = redisLockMapper.selectByPrimaryKey(1);
        // 2、查询库存，为0则活动结束
        if(redisLock.getCounts()==0){
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
            responseResult.setMsg("库存不够.");
            return responseResult;
        }
        //3、减库存
        redisLock.setCounts(redisLock.getCounts()-1);
        redisLockMapper.updateByPrimaryKeySelective(redisLock);
        try{
            Thread.sleep(5000);//模拟减库存的处理时间
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //4、释放锁
        redisLockComponent.unlock(key,value);
        responseResult.setMsg("恭喜您，秒杀成功。");
        return responseResult;
    }
}
