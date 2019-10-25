package xyz.wongs.weathertop.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.exception.expand.WeathertopAccountException;
import xyz.wongs.weathertop.shiro.sys.entity.OptMenu;
import xyz.wongs.weathertop.shiro.sys.entity.OptRole;
import xyz.wongs.weathertop.shiro.sys.entity.SAccount;
import xyz.wongs.weathertop.shiro.sys.service.OptMenuService;
import xyz.wongs.weathertop.shiro.sys.service.OptRoleService;
import xyz.wongs.weathertop.shiro.sys.service.SAccountService;

import java.util.*;

@Slf4j
public class WeathertopShiroRealm extends AuthorizingRealm {

    @Autowired
    private SAccountService sAccountService;

    @Autowired
    private OptRoleService optRoleService;

    @Autowired
    private OptMenuService optMenuService;

    private final static String ADMIN="admin";
    private final static String ADMIN_ROOT="root";

    /**
     *  权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/21 15:22
     * @param principalCollection
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @throws
     * @since
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.warn("授予角色和权限");
        // 获取当前登陆用户
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        Subject subject = SecurityUtils.getSubject();
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        if(userName.equalsIgnoreCase(ADMIN) || userName.equalsIgnoreCase(ADMIN_ROOT)){
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else{
            SAccount sAccount = getSAccount(userName);
            List<OptRole> roles = optRoleService.selectRoleByAcctId(sAccount.getId());
            if(!roles.isEmpty()){
                for(OptRole role:roles){
                    authorizationInfo.addRole(role.getRoleCode());
                    List<OptMenu> menus = optMenuService.selectMenuByRoleId(role.getId());
                    if(!menus.isEmpty()){
                        for(OptMenu menu:menus){
                            authorizationInfo.addStringPermission(menu.getMenuCode());
                        }
                    }
                }
            }

        }
        return authorizationInfo;
    }


    /** 获取即将需要认证的身份信息。即登录通过账号和密码验证登陆人的身份信息。这里可以注入userService,为了方便演示，我就写死了帐号了密码
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/21 15:23
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @throws
     * @since
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        UsernamePasswordToken upt = (UsernamePasswordToken)authenticationToken;
        String userName =upt.getUsername();
        SAccount sAccount = getSAccount(userName);
        return new SimpleAuthenticationInfo(sAccount, DigestUtils.md5Hex(sAccount.getPassword()),getName());
    }

//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//
//        if (authenticationToken.getPrincipal() == null) {
//            return null;
//        }
//        String userName = (String) authenticationToken.getPrincipal();
//        String userPwd = new String((char[]) authenticationToken.getCredentials());
//        SAccount sAccount = getSAccount(userName);
//        return new SimpleAuthenticationInfo(userName, sAccount.getPassword(),getName());
//    }

    public SAccount getSAccount(String userName){
        SAccount sAccount = new SAccount();
        sAccount.setAccountname(userName);
        List<SAccount> list = sAccountService.selectByExample(sAccount);
        Optional<List<SAccount>> emptyOpt = Optional.ofNullable(list);
        if(!emptyOpt.isPresent()){
            log.error("不存在该用户");
            new WeathertopAccountException(ResponseCode.NOT_EXISTS_USER.getMsg(),ResponseCode.NOT_EXISTS_USER.getCode());
        }
        return sAccount = emptyOpt.get().get(0);
    }
}
