package xyz.wongs.weathertop.war3.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.wongs.weathertop.base.web.BaseController;
import xyz.wongs.weathertop.war3.common.config.MsgInfo;
import xyz.wongs.weathertop.war3.system.entity.SysMenu;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
import xyz.wongs.weathertop.war3.system.service.SysConfigService;
import xyz.wongs.weathertop.war3.system.service.SysMenuService;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;

/**
 * @ClassName SysIndexController
 * @Description 首页 业务处理
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 14:29
 * @Version 1.0.0
*/
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysConfigService sysConfigService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = sysMenuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", sysConfigService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", sysConfigService.selectConfigByKey("sys.index.skinName"));
        mmap.put("copyrightYear", MsgInfo.getCopyrightYear());
        mmap.put("demoEnabled", MsgInfo.isDemoEnabled());
        return "index";
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap) {
        return "skin";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", MsgInfo.getVersion());
        return "main";
    }
}
