package xyz.wongs.weathertop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.wongs.weathertop.akkad.processor.zhihu.ZhihuTask;

@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@SpringBootApplication
public class WebMagicApplication implements CommandLineRunner {

    @Autowired
    private ZhihuTask zhihuTask;

    public static void main(String[] args) {
        SpringApplication.run(WebMagicApplication.class,args);
    }

    @Override
    public void run(String... strings) throws Exception {
        zhihuTask.crawl();
    }
}
