package xyz.wongs.weathertop;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@SpringBootApplication
@NacosPropertySource(dataId = "xyz.wongs.weathertop", groupId="SECOND_GROUP", autoRefreshed = true)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
