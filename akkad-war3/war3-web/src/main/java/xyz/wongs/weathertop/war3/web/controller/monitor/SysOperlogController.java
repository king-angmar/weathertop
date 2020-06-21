package xyz.wongs.weathertop.war3.web.controller.monitor;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.core.page.TableDataInfo;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.poi.ExcelUtil;
import xyz.wongs.weathertop.war3.system.entity.SysOperLog;
import xyz.wongs.weathertop.war3.system.service.SysOperLogService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;

/**
 * @author WCNGS@QQ.COM
 * @ClassName SysOperlogController
 * @Description 操作日志记录
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/20 21:53
 * @Version 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends AbsController {

    private static final String prefix = "monitor/operlog";

    @Autowired
    private SysOperLogService sysOperLogService;

    @RequiresPermissions("monitor:operlog:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/operlog";
    }

    @RequiresPermissions("monitor:operlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = sysOperLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @WebLog(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:operlog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOperLog operLog) {
        List<SysOperLog> list = sysOperLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysOperLogService.deleteOperLogByIds(ids));
    }

    @RequiresPermissions("monitor:operlog:detail")
    @GetMapping("/detail/{operId}")
    public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
        mmap.put("operLog", sysOperLogService.selectByPrimaryKey(operId));
        return prefix + "/detail";
    }

    @WebLog(title = "操作日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:operlog:remove")
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean() {
        sysOperLogService.cleanOperLog();
        return success();
    }

}
