package xyz.wongs.weathertop.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@NacosPropertySource(dataId = "xyz.wongs.weathertop", autoRefreshed = true)
public class DruidDbConfig {

    @NacosValue(value = "${spring.datasource.driver-class-name:com.mysql.jdbc.Driver}", autoRefreshed = true)
    String driverClassName;

    @NacosValue(value = "${spring.datasource.url:jdbc:mysql://127.0.0.1:3306}", autoRefreshed = true)
    String url;

    @NacosValue(value = "${spring.datasource.password:password}", autoRefreshed = true)
    String passWord;

    @NacosValue(value = "${spring.datasource.username:root}", autoRefreshed = true)
    String userName;

    @NacosValue(value = "${spring.datasource.druid.initial-size:20}", autoRefreshed = true)
    private int initialSize;

    @NacosValue(value = "${spring.datasource.druid.min-idle:20}", autoRefreshed = true)
    private int minIdle;

    @NacosValue(value = "${spring.datasource.druid.max-active:200}", autoRefreshed = true)
    private int maxActive;

    @NacosValue(value = "${spring.datasource.druid. max-wait:30000}", autoRefreshed = true)
    private int maxWait;

    @NacosValue(value = "${spring.datasource.druid.time-between-eviction-runs-millis:60000}", autoRefreshed = true)
    private int timeBetweenEvictionRunsMillis;

    @NacosValue(value = "${spring.datasource.druid.min-evictable-idle-time-millis:300000}", autoRefreshed = true)
    private int minEvictableIdleTimeMillis;

    @NacosValue(value = "${spring.datasource.druid.validation-query:'select 1'}", autoRefreshed = true)
    private String validationQuery;

    @NacosValue(value = "${spring.datasource.druid.test-while-idle:true}", autoRefreshed = true)
    private boolean testWhileIdle;

    @NacosValue(value = "${spring.datasource.druid.test-on-borrow:false}", autoRefreshed = true)
    private boolean testOnBorrow;

    @NacosValue(value = "${spring.datasource.druid.test-on-return:false}", autoRefreshed = true)
    private boolean testOnReturn;

    @NacosValue(value = "${spring.datasource.druid.pool-prepared-statements:false}", autoRefreshed = true)
    private boolean poolPreparedStatements;

    @NacosValue(value = "${spring.datasource.druid.max-pool-prepared-statement-per-connection-size:20}", autoRefreshed = true)
    private int maxPoolPreparedStatementPerConnectionSize;

    @NacosValue(value = "${spring.datasource.druid.filters:'stat,wall,log4j'}", autoRefreshed = true)
    private String filters;

    @NacosValue(value = "{spring.datasource.druid.connectionProperties:'druid.stat.mergeSql=true;druid.stat.slowSqlMillis=6000'}", autoRefreshed = true)
    private String connectionProperties;

    @NacosValue(value = "${spring.datasource.druid.login-username:admin}", autoRefreshed = true)
    private String loginUsername;

    @NacosValue(value = "{spring.datasource.druid.login-password:admin}", autoRefreshed = true)
    private String loginPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(userName);
        //可做加密处理
        datasource.setPassword(passWord);
        datasource.setDriverClassName(driverClassName);
        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {

        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        //设置控制台管理用户
        reg.addInitParameter("loginUsername",loginUsername);
        reg.addInitParameter("loginPassword",loginPassword);
        // 禁用HTML页面上的“Reset All”功能
        reg.addInitParameter("resetEnable","false");
        //reg.addInitParameter("allow", "127.0.0.1"); //白名单
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        //创建过滤器
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>();
        //忽略过滤的形式
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        //设置过滤器过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
