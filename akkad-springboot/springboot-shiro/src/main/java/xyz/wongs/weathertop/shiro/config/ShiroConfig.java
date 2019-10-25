package xyz.wongs.weathertop.shiro.config;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xyz.wongs.weathertop.shiro.filter.KickoutSessionFilter;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/10/23 15:21
 * @Version 1.0.0
*/
@Slf4j
@Configuration
@EnableTransactionManagement
public class ShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        log.warn("-----------------Shiro拦截器工厂类注入开始");

        bean.setSecurityManager(securityManager);

        //添加kickout认证
        HashMap<String, Filter> hashMap=new HashMap<String,Filter>();
        hashMap.put("kickout",kickoutSessionFilter());

        bean.setFilters(hashMap);

        // 指定要求登录时的链接
        bean.setLoginUrl("/toLogin");
        // 登录成功后要跳转的链接
        bean.setSuccessUrl("/home");
        // 未授权时跳转的界面;
        bean.setUnauthorizedUrl("/error");

        // filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,具体的退出代码Shiro已经实现
        filterChainDefinitionMap.put("/logout", "logout");
        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/user/userList", "user");
        filterChainDefinitionMap.put("/", "user");
//
//		// 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/css/*", "anon");
        filterChainDefinitionMap.put("/js/*", "anon");
        filterChainDefinitionMap.put("/js/*/*", "anon");
        filterChainDefinitionMap.put("/js/*/*/*", "anon");
        filterChainDefinitionMap.put("/images/*/**", "anon");
        filterChainDefinitionMap.put("/layui/*", "anon");
        filterChainDefinitionMap.put("/layui/*/**", "anon");
        filterChainDefinitionMap.put("/treegrid/*", "anon");
        filterChainDefinitionMap.put("/treegrid/*/*", "anon");
        filterChainDefinitionMap.put("/fragments/*", "anon");
        filterChainDefinitionMap.put("/layout", "anon");

        filterChainDefinitionMap.put("/user/sendMsg", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/home", "anon");
//		/*filterChainDefinitionMap.put("/page", "anon");
//		filterChainDefinitionMap.put("/channel/record", "anon");*/
        filterChainDefinitionMap.put("/user/delUser", "authc,perms[usermanage]");
//		//add操作，该用户必须有【addOperation】权限
////		filterChainDefinitionMap.put("/add", "perms[addOperation]");
//
//		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        filterChainDefinitionMap.put("/**", "kickout,authc");
        filterChainDefinitionMap.put("/*/*", "authc");
        filterChainDefinitionMap.put("/*/*/*", "authc");
        filterChainDefinitionMap.put("/*/*/*/**", "authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        log.warn("-----------------Shiro拦截器工厂类注入成功");
        return bean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(weathertopShiroRealm());
        // //注入ehcache缓存管理器;
        defaultSecurityManager.setCacheManager(ehCacheManager());
        // //注入session管理器;
        defaultSecurityManager.setSessionManager(sessionManager());
        //注入Cookie记住我管理器
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultSecurityManager;
    }

    @Bean
    public WeathertopShiroRealm weathertopShiroRealm() {
        WeathertopShiroRealm weathertopShiroRealm = new WeathertopShiroRealm();
        weathertopShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return weathertopShiroRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager());
        //new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于 // md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /** kickoutSessionFilter同一个用户多设备登录限制
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/23 15:25
     * @param
     * @return KickoutSessionFilter
     * @throws
     * @since
     */
    public KickoutSessionFilter kickoutSessionFilter(){

        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionFilter.setCacheManager(ehCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionFilter.setKickoutUrl("/toLogin?kickout=1");
        return kickoutSessionFilter;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehcache = new EhCacheManager();
        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
        if(cacheManager == null){
            try {
                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:ehcache.xml"));

            } catch (CacheException | IOException e) {
                e.printStackTrace();
            }
        }
        ehcache.setCacheManager(cacheManager);
        return ehcache;
    }


    /**EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/23 15:50
     * @param
     * @return org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
     * @throws
     * @since
     */
    @Bean
    public EnterpriseCacheSessionDAO enterCacheSessionDAO() {

        EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //添加缓存管理器
        //enterCacheSessionDAO.setCacheManager(ehCacheManager());
        //添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
        enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return enterCacheSessionDAO;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //sessionManager.setCacheManager(ehCacheManager());
        sessionManager.setSessionDAO(enterCacheSessionDAO());
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){
        log.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SimpleCookie remeberMeCookie(){
        log.debug("记住我，设置cookie过期时间！");
        //cookie名称;对应前端的checkbox的name = rememberMe
        SimpleCookie scookie=new SimpleCookie("rememberMe");
        //记住我cookie生效时间30天 ,单位秒  [10天]
        scookie.setMaxAge(864000);
        return scookie;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        //DefaultSecurityManager
        SimpleCookie simpleCookie = new SimpleCookie();
        //sessionManager.setCacheManager(ehCacheManager());
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("SHRIOSESSIONID");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }


    /**@描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
     * </br>不使用注解的话，可以注释掉这两个配置
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/23 15:50
     * @param
     * @return org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
     * @throws
     * @since
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
