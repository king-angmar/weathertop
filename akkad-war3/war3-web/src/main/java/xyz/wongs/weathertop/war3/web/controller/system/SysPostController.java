package xyz.wongs.weathertop.war3.web.controller.system;


import io.swagger.models.auth.In;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.web.BaseController;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.core.page.TableDataInfo;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.common.utils.poi.ExcelUtil;
import xyz.wongs.weathertop.war3.system.entity.SysPost;
import xyz.wongs.weathertop.war3.system.service.SysPostService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/system/post")
public class SysPostController extends AbsController {
    private String prefix = "system/post";

    @Autowired
    private SysPostService sysPostService;

    @RequiresPermissions("system:post:view")
    @GetMapping()
    public String operlog() {
        return prefix + "/post";
    }

    @RequiresPermissions("system:post:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = sysPostService.selectPostList(post);
        return getDataTable(list);
    }

    @WebLog(title = "岗位管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:post:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysPost post) {
        List<SysPost> list = sysPostService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    @RequiresPermissions("system:post:remove")
    @WebLog(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(sysPostService.deletePostByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 新增岗位
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存岗位
     */
    @RequiresPermissions("system:post:add")
    @WebLog(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysPost post) {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(sysPostService.checkPostNameUnique(post))) {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(sysPostService.checkPostCodeUnique(post))) {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(Integer.parseInt(sysPostService.insert(post).toString()));
    }

    /**
     * 修改岗位
     */
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
        mmap.put("post", sysPostService.selectByPrimaryKey(postId));
        return prefix + "/edit";
    }

    /**
     * 修改保存岗位
     */
    @RequiresPermissions("system:post:edit")
    @WebLog(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysPost post) {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(sysPostService.checkPostNameUnique(post))) {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(sysPostService.checkPostCodeUnique(post))) {
            return error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(sysPostService.updateByPrimaryKey(post));
    }

    /**
     * 校验岗位名称
     */
    @PostMapping("/checkPostNameUnique")
    @ResponseBody
    public String checkPostNameUnique(SysPost post) {
        return sysPostService.checkPostNameUnique(post);
    }

    /**
     * 校验岗位编码
     */
    @PostMapping("/checkPostCodeUnique")
    @ResponseBody
    public String checkPostCodeUnique(SysPost post) {
        return sysPostService.checkPostCodeUnique(post);
    }
}
