package xyz.wongs.weathertop.akkad.lsms.analydb.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.akkad.lsms.analydb.task.ReadRemoteFileService;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.base.persistence.mybatis.web.BaseController;
import java.io.File;


@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    ReadRemoteFileService readRemoteFileService;

    @GetMapping("/")
    public ResponseResult index() throws Exception{
        ResponseResult response = new ResponseResult();
        String tempDire = "F:\\LP\\soft";
        long startMil = System.currentTimeMillis();
        readRemoteFileService.getFileList(new File(tempDire));
        log.error((" 耗时 "+ (System.currentTimeMillis()-startMil)/1000) +" 秒");
        response.setData("执行成功");
        return response;
    }

}
