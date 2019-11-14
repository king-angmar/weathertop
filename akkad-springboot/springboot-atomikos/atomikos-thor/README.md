<!-- TOC -->

- [1. 分布式事务概念](#1-分布式事务概念)
- [2. SpringBoot整合Atomikos](#2-springboot整合atomikos)
    - [2.1. 环境概述](#21-环境概述)
    - [2.2. 添加依赖](#22-添加依赖)
    - [2.3. application文件配置](#23-application文件配置)
    - [3.1. SQL脚本](#31-sql脚本)
    - [3.2. 数据源核心配置](#32-数据源核心配置)
        - [3.2.1. DruidConfig](#321-druidconfig)
        - [3.2.2. 数据源A的配置](#322-数据源a的配置)
        - [3.2.3. 数据源B的配置](#323-数据源b的配置)
        - [3.2.4. 注意](#324-注意)
    - [3.3. 编写样例](#33-编写样例)
        - [3.3.1. 服务层](#331-服务层)
        - [3.3.2. 利用SpringBoot中的JUnit单元测试](#332-利用springboot中的junit单元测试)
- [4. 源码](#4-源码)

<!-- /TOC -->

# 1. 分布式事务概念

[分布式事务是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。](https://baike.baidu.com/item/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/4747029?fr=aladdin)

也就是说我们在操作一个业务逻辑过程中，涉及两个数据源（A、B），且很多时候A、B这两个数据源属于两个不同的物理环境。当我们操作A数据源过程中出现异常情况，那么必须让针对B数据源的操作回滚，同时A数据源的操作也回滚。

JAVA领域中针对分布式事务的解决方案就是JTA(即Java Transaction API)；本章节我们只针对SpringBoot官方提供的Atomikos 和 Bitronix的两种做描述解决思路；

# 2. SpringBoot整合Atomikos

## 2.1. 环境概述

- 开发环境：Maven+IDEA

- 组件版本

> SpringBoot版本：2.1.8.RELEASE

> mybatis-spring-boot-starter： 1.3.4

> druid-spring-boot-starter： 1.1.13

> mysql-connector-java： 5.1.40

## 2.2. 添加依赖

在POM文件中添加springboot集成atomikos的依赖，已经帮我们集成好transaction-jms、transaction-jta、transaction-jdbc、javax.transaction-api

~~~
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jta-atomikos</artifactId>
</dependency>
~~~

## 2.3. application文件配置

由于我在application.yml指定active，所以还应该有一个application-local.yml文件，这些细节大家可以根据实际情况来取舍。

- application.yml

~~~
spring:
  profiles:
    active: local
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
  jta:
    log-dir: classpath:tx-logs
    transaction-manager-id: txManager
~~~

- application-local.yml

为了案例演示的方便，所以两个用户，实际使用上大家配置为不同物理机器即可，这里就不在赘述。

~~~
server:
  port: 9090
# 3. mysql数据源配置
spring:
  datasource:
    type: com.alibaba.druid.pool.xa.DruidXADataSource
    druid:
      name: systemDb
      systemDb:
        url: jdbc:mysql://127.0.0.1:3306/np?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
        userName: root
        passWord: 123456
        initialSize: 5
        minIdle: 5
        maxActive: 20
        maxWait: 30000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: select 1
        validationQueryTimeout: 10000
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
      businessDb:
        name: businessDb
        url: jdbc:mysql://127.0.0.1:3306/springboot?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
        userName: root
        passWord: 123456
        initialSize: 5
        minIdle: 5
        maxActive: 20
        maxWait: 30000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: select 1
        validationQueryTimeout: 10000
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,wall
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000

~~~

## 3.1. SQL脚本

- springboot实例下的脚本

~~~
CREATE TABLE `tb_locations` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `flag` varchar(2) DEFAULT NULL,
   `local_code` varchar(15) NOT NULL,
   `local_name` varchar(120) NOT NULL,
   `lv` int(11) DEFAULT NULL,
   `sup_local_code` varchar(15) NOT NULL,
   `url` varchar(20) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=319756 DEFAULT CHARSET=utf8
~~~

- np实例下的脚本

~~~
CREATE TABLE `inform_sms` (
   `inform_id` int(11) NOT NULL COMMENT '通知ID',
   `service_type` varchar(20) DEFAULT NULL COMMENT '业务类型',
   `service_id` varchar(40) DEFAULT NULL COMMENT '业务ID',
   `content` varchar(4000) DEFAULT NULL COMMENT '内容',
   `triger_time` date DEFAULT NULL COMMENT '触发时间',
   `create_date` date DEFAULT NULL COMMENT '创建时间',
   `state` varchar(10) DEFAULT NULL COMMENT '状态',
   `state_remark` varchar(1000) DEFAULT NULL COMMENT '状态备注',
   `state_date` date DEFAULT NULL COMMENT '声明时间',
   PRIMARY KEY (`inform_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8
~~~

## 3.2. 数据源核心配置

既然两套不通数据源，我们就应该有两套不通数据源的配置、注册、事务管理等，因为我使用Druid，所以这里就演示用Druid的配置，其他配置，大家可自行在网上找材料，也不是很难。

以下为核心代码

### 3.2.1. DruidConfig

~~~
package xyz.wongs.weathertop.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
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
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.systemDb.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("systemDb");
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
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
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        Properties prop = build(env, "spring.datasource.druid.businessDb.");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("businessDb");
        ds.setPoolSize(5);
        ds.setXaProperties(prop);
        return ds;
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
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
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

~~~

### 3.2.2. 数据源A的配置

~~~
package xyz.wongs.weathertop.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = SystemDataSourceConfig.PACKAGE,markerInterface = BaseMapper.class, sqlSessionFactoryRef = "systemSqlSessionFactory")
public class SystemDataSourceConfig {

	static final String PACKAGE = "xyz.wongs.weathertop.mapper.quanmin";

	@Autowired
	@Qualifier("systemDataSource")
	private DataSource ds;

	@Bean
	@Primary
	public SqlSessionFactory systemSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(ds);
		//指定mapper xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/system/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}


	@Bean
	public SqlSessionTemplate sqlSessionTemplateSystem() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(systemSqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}
}

~~~

### 3.2.3. 数据源B的配置

~~~
package xyz.wongs.weathertop.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = BusinessDataSourceConfig.PACKAGE,markerInterface = BaseMapper.class, sqlSessionFactoryRef = "businessSqlSessionFactory")
public class BusinessDataSourceConfig {

	static final String PACKAGE = "xyz.wongs.weathertop.mapper.location";

	@Autowired
	@Qualifier("businessDataSource")
	private DataSource ds;

	@Bean(name = "businessSqlSessionFactory")
	public SqlSessionFactory businessSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(ds);
		//指定mapper xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/business/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplateBusiness() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(businessSqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}

}

~~~

### 3.2.4. 注意

在演示样例中只使用一个事务管理器：xatx，并没有使用TxAdviceInterceptor.java和TxAdvice2Interceptor.java中配置的事务管理器；有需求的童鞋可以自己配置其他的事务管理器；(见DruidConfig.java中查看) 

## 3.3. 编写样例

### 3.3.1. 服务层

~~~
package xyz.wongs.weathertop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.entity.quanmin.InformSms;
import xyz.wongs.weathertop.service.location.LocationService;
import xyz.wongs.weathertop.service.quanmin.InformSmsService;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
public class JtaService {

    @Autowired
    private InformSmsService informSmsService;

    @Autowired
    private LocationService locationService;

    @Transactional(readOnly = false)
    public void testJTA() {

        Location location = new Location();
        location.setFlag("J");
        location.setLocalCode("2324");
        location.setLocalName("测试");
        location.setLv(9);
        location.setSupLocalCode("213");
        location.setUrl("www.baidu.com");

        locationService.insert(location);

        InformSms informSms = new InformSms();
        informSms.setContent("joda");
        informSms.setCreateDate(new Date());
        informSms.setServiceType("wy");
        informSms.setStateDate(new Date());
        informSms.setStateRemark("测试分布式事务");
        informSmsService.insert(informSms);

//		int i = 10/0;
    }

}

~~~

### 3.3.2. 利用SpringBoot中的JUnit单元测试

~~~
@Autowired
private JtaService jtaService;

@Test
public void testJTA(){
    jtaService.testJTA();
}

~~~

# 4. 源码

[Github演示源码]([https://github.com/king-angmar/weathertop/tree/master/akkad-springboot/springboot-atomikos/atomikos-thor]) ，记得给Star