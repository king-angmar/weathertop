package xyz.wongs.weathertop.shiro.web;

import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.shiro.sys.entity.SAccount;
import xyz.wongs.weathertop.shiro.sys.service.SAccountService;
import xyz.wongs.weathertop.shiro.vo.AcctVo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SAccountService sAccountService;

    @Autowired
    private EhCacheManager ecm;

    @RequestMapping("/userList")
    public String toUserList() {
        return "/auth/userList";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult login(AcctVo acctVo,@RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
        log.debug("用户登录，请求参数=user:" + acctVo.toString() + "，是否记住我：" + rememberMe);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseCode.ERROR.getCode());
        if (null == acctVo) {
            responseResult.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
            responseResult.setMsg("请求参数有误，请您稍后再试");
            log.debug("用户登录，结果=responseResult:" + responseResult);
            return responseResult;
        }
        if (!validatorRequestParam(acctVo, responseResult)) {
            log.debug("用户登录，结果=responseResult:" + responseResult);
            return responseResult;
        }
        // 用户是否存在
        SAccount account = new SAccount();
        account.setAccountname(acctVo.getUsername());

        List<SAccount> list = sAccountService.selectByExample(account);
        Optional<List<SAccount>> emptyOpt = Optional.ofNullable(list);
        if(!emptyOpt.isPresent()){
            responseResult.setMsg("该用户不存在，请您联系管理员");
            log.debug("用户登录，结果=responseResult:" + responseResult);
            return responseResult;
        }
        account = sAccountService.selectByExample(account).get(0);

        try {
            // 1、 封装用户名、密码、是否记住我到token令牌对象 [支持记住我]
            AuthenticationToken token = new UsernamePasswordToken(acctVo.getUsername(), DigestUtils.md5Hex(acctVo.getPassword()),rememberMe);
            // 2、 Subject调用login
            Subject subject = SecurityUtils.getSubject();
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.debug("用户登录，用户验证开始！user=" + acctVo.getUsername());
            subject.login(token);
            responseResult = new ResponseResult();
            log.info("用户登录，用户验证通过！user=" + acctVo.getUsername());
        } catch (UnknownAccountException uae) {
            log.error("用户登录，用户验证未通过：未知用户！user={},异常信息: {}" , acctVo.getUsername(), uae.getMessage());
            responseResult.setMsg("该用户不存在，请您联系管理员");
        } catch (IncorrectCredentialsException ice) {
            // 获取输错次数
            log.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user={},异常信息: {}" , acctVo.getUsername(),ice.getMessage());
            responseResult.setMsg("用户名或密码不正确");
        } catch (LockedAccountException lae) {
            log.error("用户登录，用户验证未通过：账户已锁定！user={},异常信息: {}" + acctVo.getUsername(), lae.getMessage());
            responseResult.setMsg("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            log.error("用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user={},异常信息: {}" , acctVo, eae);
            responseResult.setMsg("用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
            // 这里结合了，另一种密码输错限制的实现，基于redis或mysql的实现；也可以直接使用RetryLimitHashedCredentialsMatcher限制5次
        } catch (AuthenticationException ae) {
            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.error("用户登录，用户验证未通过：认证异常，异常信息如下！user={},异常信息: {}" , acctVo.getUsername(),ae.getMessage());
            responseResult.setMsg("用户名或密码不正确");
        } catch (Exception e) {
            log.error("用户登录，用户验证未通过：操作异常，异常信息如下！user={},异常信息: {}" , acctVo.getUsername(), e.getMessage());
            responseResult.setMsg("用户登录失败，请您稍后再试");
        }
        Cache<String, AtomicInteger> passwordRetryCache = ecm.getCache("passwordRetryCache");
        if (null != passwordRetryCache) {
            int retryNum = (passwordRetryCache.get(account.getAccountname()) == null ? 0: passwordRetryCache.get(account.getAccountname())).intValue();
            log.debug("输错次数：" + retryNum);
            if (retryNum > 0 && retryNum < 6) {
                responseResult.setMsg("用户名或密码错误" + retryNum + "次,再输错"+ (6 - retryNum) + "次账号将锁定");
            }
        }
        log.debug("用户登录，user=" + account.getAccountname() + ",登录结果=responseResult:" + responseResult);
        return responseResult;
    }

    protected boolean validatorRequestParam(Object obj, ResponseResult response) {
        boolean flag = false;
        Validator validator = new Validator();
        List<ConstraintViolation> ret = validator.validate(obj);
        if (ret.size() > 0) {
            // 校验参数有误
            response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
            response.setMsg(ret.get(0).getMessageTemplate());
        } else {
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("123456"));
    }
}
