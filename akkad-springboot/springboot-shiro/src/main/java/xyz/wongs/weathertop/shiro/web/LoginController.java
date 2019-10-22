package xyz.wongs.weathertop.shiro.web;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.wongs.weathertop.shiro.vo.AcctVo;

import java.util.Map;

@Slf4j
@Controller
public class LoginController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody AcctVo acctVo){
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        if(null!=subject){
            log.warn(" 已经登录 ");
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                acctVo.getUserName(),
                acctVo.getPassWord());
        //进行验证，这里可以捕获异常，然后返回对应信息
        try {
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return "login";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/logout")
    public String logout(){
        return "logout";
    }

    public static void main(String[] args) {
        AcctVo vo = new AcctVo("wp","wongs");
        System.out.println(JSONObject.toJSON(vo));

    }
}
