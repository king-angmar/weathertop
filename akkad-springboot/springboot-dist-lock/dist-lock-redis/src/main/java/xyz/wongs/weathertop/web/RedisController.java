package xyz.wongs.weathertop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public String Seckilling(@PathVariable("key") String key){
        //加锁
        String value = System.currentTimeMillis() + "";
        if(!redisLockComponent.getLock(key,value,TIMEOUT, TimeUnit.SECONDS,3,6000)){
            return "排队人数太多，请稍后再试.";
        }

        RedisLock redisLock = redisLockMapper.selectByPrimaryKey(1);
        // 查询该商品库存，为0则活动结束 e.g. getStockByTargetId
        if(redisLock.getCounts()==0){
            return "活动结束.";
        }else {
            // 下单 e.g. buyStockByTargetId

            //减库存 不做处理的话，高并发下会出现超卖的情况，下单数，大于减库存的情况。虽然这里减了，但由于并发，减的库存还没存到map中去。新的并发拿到的是原来的库存
            redisLock.setCounts(redisLock.getCounts()-1);
            redisLockMapper.updateByPrimaryKeySelective(redisLock);
            try{
                Thread.sleep(5000);//模拟减库存的处理时间
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            // 减库存操作数据库 e.g. updateStockByTargetId

            // buyStockByTargetId 和 updateStockByTargetId 可以同步完成(或者事物)，保证原子性。
        }
        //解锁
        redisLockComponent.unlock(key,value);

        return "恭喜您，秒杀成功。";
    }
}
