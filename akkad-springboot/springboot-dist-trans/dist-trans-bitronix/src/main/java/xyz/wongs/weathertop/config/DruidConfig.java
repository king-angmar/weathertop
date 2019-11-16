package xyz.wongs.weathertop.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.Properties;


/**
 * @ClassName DruidConfig
 * @Description 分布式事务数据源配置
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/11/14 17:39
 * @Version 1.0.0
*/
@Configuration
public class DruidConfig {

    /**
     * @Description 数据源A的配置
     * @param env
     * @return javax.sql.DataSource
     * @throws
     * @date 2019/11/14 17:40
     */
    @Bean(name = "systemDataSource")
    @Primary
    @Autowired
    public DataSource systemDataSource(Environment env) {

        return null;
    }


    /**
     * @Description 数据源B的配置
     * @param env
     * @return javax.sql.DataSource
     * @throws
     * @date 2019/11/14 17:40
     */
    @Autowired
    @Bean(name = "businessDataSource")
    public AtomikosDataSourceBean businessDataSource(Environment env) {

        return null;
    }


    /**
     * @Description 注入事物管理器
     * @param
     * @return org.springframework.transaction.jta.JtaTransactionManager
     * @throws
     * @date 2019/11/14 17:41
     */
    @Bean(name = "xatx")
    public JtaTransactionManager regTransactionManager () {
//        UserTransactionManager userTransactionManager = new UserTransactionManager();
//        UserTransaction userTransaction = new UserTransactionImp();
//        return new JtaTransactionManager(userTransaction, userTransactionManager);
        return null;
    }

    /**
     * @Description 配置读取通用的方法
     * @param env   环境
     * @param prefix    前缀
     * @return java.util.Properties
     * @throws
     * @date 2019/11/14 17:41
     */
    private Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "userName"));
        prop.put("password", env.getProperty(prefix + "passWord"));
        prop.put("initialSize", env.getProperty(prefix + "initialSize", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "maxActive", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "maxWait", Integer.class));
        prop.put("timeBetweenEvictionRunsMillis",env.getProperty(prefix + "timeBetweenEvictionRunsMillis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis", Integer.class));
        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements", Boolean.class));
        prop.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
        prop.put("connectionProperties", env.getProperty(prefix + "connectionProperties"));
        return prop;
    }

    /**
     * @Description 添加对druid的安全访问
     * @param
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean
     * @throws
     * @date 2019/11/14 17:42
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //控制台管理用户，加入下面2行 进入druid后台就需要登录
        //servletRegistrationBean.addInitParameter("loginUsername", "admin");
        //servletRegistrationBean.addInitParameter("loginPassword", "admin");
        return servletRegistrationBean;
    }

    /**
     * @Description
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @throws
     * @date 2019/11/14 17:42
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(1000);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }
}
