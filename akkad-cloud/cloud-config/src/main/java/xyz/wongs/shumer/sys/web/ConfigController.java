package xyz.wongs.shumer.sys.web;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConfigController {

    @NacosValue(value = "${nacos.test.propertie:123}", autoRefreshed = true)
    private String testProperties;

    @NacosValue(value = "${spring.datasource.driver-class-name:com.mysql.jdbc.Driver}", autoRefreshed = true)
    String driverClassName;

    @GetMapping("/test")
    public String test(){
        log.error("=====");
        return driverClassName;
    }
}
