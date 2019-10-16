package xyz.wongs.weathertop.akkad.lsms;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.wongs.weathertop.akkad.lsms.analydb.task.ReadRemoteFileService;
import xyz.wongs.weathertop.akkad.lsms.base.BaseAppTest;

import java.io.File;
import java.io.IOException;


@Slf4j
public class TaskApplicationTest extends BaseAppTest {

    @Autowired
    ReadRemoteFileService readRemoteFileService;

    @Test
    public void testInsertDb() throws IOException {
        String tempDire = "F:\\LP\\soft";
        long startMil = System.currentTimeMillis();
        readRemoteFileService.getFileList(new File(tempDire));
        System.out.println((" 耗时 "+ (System.currentTimeMillis()-startMil)/1000) +" 秒");
    }


}
