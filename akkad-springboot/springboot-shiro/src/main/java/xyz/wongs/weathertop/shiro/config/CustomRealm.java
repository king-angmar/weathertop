package xyz.wongs.weathertop.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import xyz.wongs.weathertop.base.message.exception.expand.WeathertopAccountException;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomRealm extends AuthorizingRealm {


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
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();
        stringSet.add("user:show");
        stringSet.add("user:admin");
        info.setStringPermissions(stringSet);
        return info;
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

        log.warn("-------身份认证方法--------");
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }

        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());

        //根据用户名从数据库获取密码
        String password = "123";
        if (userName == null) {
            new WeathertopAccountException("用户名不正确");
        } else if (!userPwd.equals(password )) {
            new WeathertopAccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(userName, password,getName());
    }
}
