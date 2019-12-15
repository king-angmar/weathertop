package xyz.wongs.weathertop;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class CloudConfigNacosApp {
    public static void main(String[] args) {
        SpringApplication.run(CloudConfigNacosApp.class,args);
    }

}
