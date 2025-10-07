package xyz.wongs.weathertop.war3.web.controller.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.common.annotation.WebLog;
import xyz.wongs.weathertop.war3.common.config.MsgInfo;
import xyz.wongs.weathertop.war3.common.domain.AjaxResult;
import xyz.wongs.weathertop.war3.common.enums.BusinessType;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.common.utils.file.FileUploadUtils;
import xyz.wongs.weathertop.war3.framework.shiro.service.SysPasswordService;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
import xyz.wongs.weathertop.war3.system.service.SysUserService;
import xyz.wongs.weathertop.war3.web.controller.base.AbsController;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Slf4j
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends AbsController {

    private String prefix = "system/user/profile";

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPasswordService passwordService;



    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", sysUserService.selectUserRoleGroup(user.getId()));
        mmap.put("postGroup", sysUserService.selectUserPostGroup(user.getId()));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password) {
        SysUser user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password)) {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", sysUserService.selectByPrimaryKey(user.getId()));
        return prefix + "/resetPwd";
    }

    @WebLog(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword) {
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)) {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
            if (sysUserService.resetUserPwd(user) > 0) {
                ShiroUtils.setSysUser(sysUserService.selectByPrimaryKey(user.getId()));
                return success();
            }
            return error();
        } else {
            return error("修改密码失败，旧密码错误");
        }
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", sysUserService.selectByPrimaryKey(user.getId()));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap) {
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", sysUserService.selectByPrimaryKey(user.getId()));
        return prefix + "/avatar";
    }

    /**
     * 修改用户
     */
    @WebLog(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysUser user) {
        SysUser currentUser = ShiroUtils.getSysUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setSex(user.getSex());
        if (sysUserService.updateUserInfo(currentUser) > 0) {
            ShiroUtils.setSysUser(sysUserService.selectByPrimaryKey(currentUser.getId()));
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @WebLog(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        SysUser currentUser = ShiroUtils.getSysUser();
        try {
            if (!file.isEmpty()) {
                String avatar = FileUploadUtils.upload(MsgInfo.getAvatarPath(), file);
                currentUser.setAvatar(avatar);
                if (sysUserService.updateUserInfo(currentUser) > 0) {
                    ShiroUtils.setSysUser(sysUserService.selectByPrimaryKey(currentUser.getId()));
                    return success();
                }
            }
            return error();
        } catch (Exception e) {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
}
