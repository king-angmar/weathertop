package xyz.wongs.weathertop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@EnableDiscoveryClient
@SpringBootApplication
public class DiscoveryProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryProviderApp.class,args);
    }
}
