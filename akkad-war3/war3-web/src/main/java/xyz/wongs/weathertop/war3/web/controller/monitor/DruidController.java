package xyz.wongs.weathertop.war3.web.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

/**
 * @author WCNGS@QQ.COM
 * @ClassName DruidController
 * @Description druid 监控
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/20 21:28
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/monitor/data")
public class DruidController extends AbsController {

    private final static String prefix = "/druid";

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index() {
        return redirect(prefix + "/index");
    }
}
