package xyz.wongs.weathertop.war3.common.config;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@ComponentScan(value = "xyz.wongs.weathertop", excludeFilters = { @Filter(Controller.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { RootConfiguration.class }) })
@MapperScan({"xyz.wongs.weathertop.**.mapper"})
public class RootConfiguration extends SpringBootServletInitializer {

    @Autowired
    private SqlSession sqlSession;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.registerShutdownHook(false);
        return builder.sources(RootConfiguration.class);
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        //Constant.threadPool = Executors.newFixedThreadPool(20);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*Mapper.xml");
        new MapperRefresh(resources, sqlSession.getConfiguration()).run();
    }

}
