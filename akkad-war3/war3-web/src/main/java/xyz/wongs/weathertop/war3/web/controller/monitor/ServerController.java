package xyz.wongs.weathertop.war3.web.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.wongs.weathertop.war3.framework.web.domain.Server;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

/**
 * @ClassName ServerController
 * @Description 服务器监控
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/20 21:30
 * @Version 1.0.0
*/
@Controller
@RequestMapping("/monitor/server")
public class ServerController extends AbsController {
    private static final String prefix = "monitor/server";

    @RequiresPermissions("monitor:server:view")
    @GetMapping()
    public String server(ModelMap mmap) throws Exception {
        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        return prefix + "/server";
    }
}
