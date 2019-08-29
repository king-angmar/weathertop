package xyz.wongs.shumer;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(basePackages = {"xyz.wongs.shumer.**.mapper"})
@SpringBootApplication
@NacosPropertySource(dataId = "xyz.wongs.shumer", groupId="SECOND_GROUP", autoRefreshed = true)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
