package xyz.wongs;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
//@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@Slf4j
@SpringBootApplication
public class AtomikosApplication  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AtomikosApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AtomikosApplication.class,args);
    }
}
