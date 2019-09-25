package xyz.wongs.weathertop.base.config;//package cn.ffcs.np.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//
///** 获取Web端口的工具类，方便记录日志
// * @ClassName ServerAppUtils
// * @Description
// * @author WCNGS@QQ.COM
// * @Github <a>https://github.com/rothschil</a>
// * @date 2019/9/2 13:45
// * @Version 1.0.0
//*/
//@Component
//@Slf4j
//public class ServerAppUtils implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
//
//    private static EmbeddedServletContainerInitializedEvent event;
//
//    @Override
//    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
//        ServerAppUtils.event = event;
//    }
//
//    public static int getPort() {
//        Assert.notNull(event);
//        int port = event.getEmbeddedServletContainer().getPort();
//        Assert.state(port != -1, "端口号获取失败");
//        return port;
//    }
//
//}
