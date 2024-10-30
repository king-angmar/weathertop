package xyz.wongs.weathertop.war3.web.controller.system;

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
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.system.entity.SysNotice;
import xyz.wongs.weathertop.war3.system.service.SysNoticeService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;


/**
 * @ClassName SysNoticeController
 * @Description 公告 信息操作处理
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/20 21:14
 * @Version 1.0.0
*/
@Slf4j
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends AbsController {

    private final static String prefix = "system/notice";

    @Autowired
    private SysNoticeService sysNoticeService;

    @RequiresPermissions("system:notice:view")
    @GetMapping()
    public String notice() {
        return prefix + "/notice";
    }

    /**
     * 查询公告列表
     */
    @RequiresPermissions("system:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = sysNoticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("system:notice:add")
    @WebLog(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotice notice) {
        notice.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(Integer.parseInt(sysNoticeService.insert(notice).toString()));
    }

    /**
     * 修改公告
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", sysNoticeService.selectByPrimaryKey(noticeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("system:notice:edit")
    @WebLog(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice) {
        notice.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(sysNoticeService.updateByPrimaryKey(notice));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("system:notice:remove")
    @WebLog(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysNoticeService.deleteNoticeByIds(ids));
    }
}
