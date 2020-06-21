package xyz.wongs.weathertop.war3.framework.shiro.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.wongs.weathertop.base.constant.Constants;
import xyz.wongs.weathertop.base.utils.MessageUtils;
import xyz.wongs.weathertop.base.utils.ServletUtils;
import xyz.wongs.weathertop.war3.common.constant.ShiroConstants;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.framework.manager.AsyncManager;
import xyz.wongs.weathertop.war3.framework.manager.factory.AsyncFactory;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
import xyz.wongs.weathertop.war3.system.service.SysUserService;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPasswordService sysPasswordService;

    /**
     * 注册
     */
    public String register(SysUser user) {
        String msg = "", username = user.getLoginName(), password = user.getPassword();

        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            msg = "验证码错误";
        } else if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(username))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(sysPasswordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
            boolean regFlag = sysUserService.registerUser(user);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }
}
