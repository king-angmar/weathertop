package xyz.wongs.weathertop.war3.web.controller.monitor;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.core.page.TableDataInfo;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.poi.ExcelUtil;
import xyz.wongs.weathertop.war3.framework.shiro.service.SysPasswordService;
import xyz.wongs.weathertop.war3.system.entity.SysLoginInfo;
import xyz.wongs.weathertop.war3.system.service.SysLoginInfoService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/monitor/logininfor")
public class SysLoginInfoController extends AbsController {
    private static final String prefix = "monitor/logininfor";

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private SysPasswordService passwordService;

    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String logininfor() {
        return prefix + "/logininfor";
    }

    @RequiresPermissions("monitor:logininfor:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLoginInfo sysLoginInfo) {
        startPage();
        List<SysLoginInfo> list = sysLoginInfoService.selectSysLoginInfoList(sysLoginInfo);
        return getDataTable(list);
    }

    @WebLog(title = "登陆日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:logininfor:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLoginInfo sysLoginInfo) {
        List<SysLoginInfo> list = sysLoginInfoService.selectSysLoginInfoList(sysLoginInfo);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<SysLoginInfo>(SysLoginInfo.class);
        return util.exportExcel(list, "登陆日志");
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @WebLog(title = "登陆日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysLoginInfoService.deleteLoginInfoByIds(ids));
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @WebLog(title = "登陆日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        sysLoginInfoService.cleanLoginInfo();
        return success();
    }

    @RequiresPermissions("monitor:logininfor:unlock")
    @WebLog(title = "账户解锁", businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlock(String loginName) {
        passwordService.unlock(loginName);
        return success();
    }
}
