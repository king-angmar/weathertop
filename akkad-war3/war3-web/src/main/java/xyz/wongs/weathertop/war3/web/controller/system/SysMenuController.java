package xyz.wongs.weathertop.war3.web.controller.system;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.domain.Ztree;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.system.entity.SysMenu;
import xyz.wongs.weathertop.war3.system.entity.SysRole;
import xyz.wongs.weathertop.war3.system.service.SysMenuService;
import xyz.wongs.weathertop.war3.system.service.SysRoleMenuService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

import java.util.List;

/**
 * 菜单信息
 * 
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends AbsController{
    private String prefix = "system/menu";

    @Autowired
    private SysMenuService sysMenuService;

    private SysRoleMenuService sysRoleMenuService;

    @RequiresPermissions("system:menu:view")
    @GetMapping()
    public String menu()
    {
        return prefix + "/menu";
    }

    @RequiresPermissions("system:menu:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysMenu> list(SysMenu menu)
    {
        Long userId = ShiroUtils.getUserId();
        List<SysMenu> menuList = sysMenuService.selectMenuList(menu, userId);
        return menuList;
    }

    /**
     * 删除菜单
     */
    @WebLog(title = "菜单管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:menu:remove")
    @GetMapping("/remove/{menuId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (sysMenuService.selectCountMenuByParentId(menuId) > 0)
        {
            return AjaxResult.warn("存在子菜单,不允许删除");
        }
        if (sysRoleMenuService.selectCountRoleMenuByMenuId(menuId) > 0)
        {
            return AjaxResult.warn("菜单已分配,不允许删除");
        }
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(sysMenuService.deleteByPrimaryKey(menuId));
    }

    /**
     * 新增
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        SysMenu menu = null;
        if (0L != parentId)
        {
            menu = sysMenuService.selectByPrimaryKey(parentId);
        }
        else
        {
            menu = new SysMenu();
            menu.setId(0L);
            menu.setMenuName("主目录");
        }
        mmap.put("menu", menu);
        return prefix + "/add";
    }

    /**
     * 新增保存菜单
     */
    @WebLog(title = "菜单管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:menu:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysMenu menu)
    {
        if (UserConstants.MENU_NAME_NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu)))
        {
            return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setCreateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(Integer.parseInt(sysMenuService.insert(menu).toString()));
    }

    /**
     * 修改菜单
     */
    @GetMapping("/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap)
    {
        mmap.put("menu", sysMenuService.selectByPrimaryKey(menuId));
        return prefix + "/edit";
    }

    /**
     * 修改保存菜单
     */
    @WebLog(title = "菜单管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:menu:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysMenu menu)
    {
        if (UserConstants.MENU_NAME_NOT_UNIQUE.equals(sysMenuService.checkMenuNameUnique(menu)))
        {
            return error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setUpdateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(sysMenuService.updateByPrimaryKey(menu));
    }

    /**
     * 选择菜单图标
     */
    @GetMapping("/icon")
    public String icon()
    {
        return prefix + "/icon";
    }

    /**
     * 校验菜单名称
     */
    @PostMapping("/checkMenuNameUnique")
    @ResponseBody
    public String checkMenuNameUnique(SysMenu menu)
    {
        return sysMenuService.checkMenuNameUnique(menu);
    }

    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleMenuTreeData")
    @ResponseBody
    public List<Ztree> roleMenuTreeData(SysRole role)
    {
        Long userId = ShiroUtils.getUserId();
        List<Ztree> ztrees = sysMenuService.roleMenuTreeData(role, userId);
        return ztrees;
    }

    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/menuTreeData")
    @ResponseBody
    public List<Ztree> menuTreeData()
    {
        Long userId = ShiroUtils.getUserId();
        List<Ztree> ztrees = sysMenuService.menuTreeData(userId);
        return ztrees;
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap)
    {
        mmap.put("menu", sysMenuService.selectByPrimaryKey(menuId));
        return prefix + "/tree";
    }
}