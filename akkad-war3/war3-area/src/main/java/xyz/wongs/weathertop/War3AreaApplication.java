package xyz.wongs.weathertop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@SpringBootApplication
public class War3AreaApplication {

    public static void main(String[] args) {
        SpringApplication.run(War3AreaApplication.class,args);
    }

}
