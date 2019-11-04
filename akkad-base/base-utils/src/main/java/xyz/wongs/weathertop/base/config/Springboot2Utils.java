package xyz.wongs.weathertop.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/** 获取Web端口的工具类，方便记录日志
 * @ClassName ServerAppUtils
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/9/2 13:45
 * @Version 1.0.0
*/
@Component
@Slf4j
public class Springboot2Utils implements ApplicationListener<ServletWebServerInitializedEvent> {

    private int serverPort;

    public int getPort() {
        return this.serverPort;
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        int port = event.getApplicationContext().getWebServer().getPort();
        Assert.state(port != -1, "端口号获取失败");
    }

}
