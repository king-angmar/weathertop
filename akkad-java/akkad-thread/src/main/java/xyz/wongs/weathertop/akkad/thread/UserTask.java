package xyz.wongs.weathertop.akkad.thread;

import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.akkad.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Component
public class UserTask implements Callable<List<SysUser>> {

    public List<SysUser> call() throws Exception {
        List<SysUser> lists = new ArrayList<SysUser>(10);
        SysUser sysUser = null;
        Thread.sleep(3000);
        for(int i=0;i<8;i++){
            sysUser = new SysUser();
            sysUser.setUuid(ToolUtils.getUuid());
            lists.add(sysUser);
        }
        return lists;
    }


}
