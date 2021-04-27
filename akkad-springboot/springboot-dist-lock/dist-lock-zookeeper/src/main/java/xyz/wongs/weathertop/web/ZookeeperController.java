package xyz.wongs.weathertop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.conf.DistributedLockByCurator;

@RestController
@Slf4j
public class ZookeeperController {

    @Autowired
    private DistributedLockByCurator distributedLockByZookeeper;

    private final static String PATH = "test";

    @GetMapping("/lock10")
    public ResponseResult getLock1() {
        ResponseResult responseResult = new ResponseResult();
        Boolean acquire = distributedLockByZookeeper.acquireDistributedLock(PATH);
        try {
            if(acquire) {
                log.error("I am lock1，i am updating resource……！！！");
                Thread.sleep(2000);
            } else{
                responseResult.setCode(ResponseCode.SYSNC_LOCK.getCode());
                responseResult.setMsg(ResponseCode.SYSNC_LOCK.getMsg());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            distributedLockByZookeeper.releaseDistributedLock(PATH);
        }
        return responseResult;
    }

    @GetMapping("/lock11")
    public ResponseResult getLock11() {
        ResponseResult responseResult = new ResponseResult();
        Boolean acquire = distributedLockByZookeeper.acquireDistributedLock(PATH);
        try {
            if(acquire) {
                log.error("I am lock11，i am updating resource……！！！");
                Thread.sleep(3000);
            } else{
                responseResult.setCode(ResponseCode.SYSNC_LOCK.getCode());
                responseResult.setMsg(ResponseCode.SYSNC_LOCK.getMsg());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            distributedLockByZookeeper.releaseDistributedLock(PATH);
        }
        return responseResult;
    }
}
