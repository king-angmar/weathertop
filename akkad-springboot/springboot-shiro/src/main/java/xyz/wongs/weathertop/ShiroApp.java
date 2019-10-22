package xyz.wongs.weathertop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@SpringBootApplication
public class ShiroApp {
    public static void main(String[] args) {
        SpringApplication.run(ShiroApp.class,args);
    }
}
