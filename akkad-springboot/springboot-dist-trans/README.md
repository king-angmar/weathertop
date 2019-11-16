<!-- TOC -->

- [1. 分布式事务概念](#1-分布式事务概念)
- [2. 产生原因](#2-产生原因)
    - [2.1. 数据库分库分表](#21-数据库分库分表)
    - [2.2. 应用服务化](#22-应用服务化)
- [3. 事务ACID特性](#3-事务acid特性)
- [4. 应用场景](#4-应用场景)
    - [4.1. 支付](#41-支付)
    - [4.2. 在线订单](#42-在线订单)
- [5. 行业中常见解决方案](#5-行业中常见解决方案)
    - [5.1. 本地消息表（异步确保）](#51-本地消息表异步确保)
    - [5.2. 两阶段提交](#52-两阶段提交)
    - [5.3. 事务消息+最终一致性](#53-事务消息最终一致性)
    - [5.4. 补偿事务（TCC）](#54-补偿事务tcc)
- [6. SpringBoot整合Atomikos](#6-springboot整合atomikos)
    - [6.1. 环境概述](#61-环境概述)
    - [6.2. 添加依赖](#62-添加依赖)
    - [6.3. application文件配置](#63-application文件配置)
    - [6.4. SQL脚本](#64-sql脚本)
    - [6.5. 数据源核心配置](#65-数据源核心配置)
        - [6.5.1. DruidConfig](#651-druidconfig)
        - [6.5.2. 数据源A的配置](#652-数据源a的配置)
        - [6.5.3. 数据源B的配置](#653-数据源b的配置)
        - [6.5.4. 注意](#654-注意)
    - [6.6. 编写样例](#66-编写样例)
        - [6.6.1. 服务层](#661-服务层)
        - [6.6.2. 利用SpringBoot中的JUnit单元测试](#662-利用springboot中的junit单元测试)
- [7. SpringBoot整合Bitronix](#7-springboot整合bitronix)
- [8. 源码](#8-源码)

<!-- /TOC -->

# 1. 分布式事务概念

讨论分布式事务之前我们分清两个概念：本地事务、分布式事务；

本地事务是解决单个数据源上的数据操作的一致性问题的话，而分布式事务则是为了解决跨越多个数据源上数据操作的一致性问题。

[百度官方对分布式事务的定义是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。](https://baike.baidu.com/item/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1/4747029?fr=aladdin)

也就是说我们在操作一个业务逻辑过程中，涉及两个数据源（A、B），且很多时候A、B这两个数据源属于两个不同的物理环境。当我们操作A数据源过程中出现异常情况，那么必须让针对B数据源的操作回滚，同时A数据源的操作也回滚。

JAVA领域中针对分布式事务的解决方案就是JTA(即Java Transaction API)；本章节我们只针对SpringBoot官方提供的Atomikos 和 Bitronix的两种做描述解决思路；

# 2. 产生原因

## 2.1. 数据库分库分表

当数据库单表数据量超过2000W，就要考虑分库分表，这时候，如果一个操作既访问01库，又访问02库，而且要保证数据的一致性，那么就要用到分布式事务。

![20191115170217.png](https://i.loli.net/2019/11/15/qNUpcKEFmwGYv2a.png)

## 2.2. 应用服务化

业务的服务化。比如原来单机支撑的应用服务，拆解为一块一块独立的服务，例如用户中心、订单中心、账户中心、库存中心。对于订单中心，有专门的数据库存储订单信息，用户中心也有专门的数据库存储用户信息，库存中心也会有专门的数据库存储库存信息。这时候如果要同时对订单进行操作，那么就会涉及到订单数据库和账户数据库，为了保证数据一致性，就需要用到分布式事务。

![20191115171120.png](https://i.loli.net/2019/11/15/94pzL71q8gDQlu3.png)

# 3. 事务ACID特性

类别 | 描述
:--:|:-
原子性 | 整个事务中的所有操作，要么全部完成，要么全部不做，没有中间状态。对于事务在执行中发生错误，所有的操作都会被回滚，整个事务就像从没被执行过一样
一致性 | 事务的执行必须保证系统的一致性，就拿转账为例，A有500元，B有300元，如果在一个事务里A成功给B转账50元，那么不管并发多少，不管发生什么，只要事务执行成功了，那么最后A账户一定是450元，B账户一定是350元。
隔离性 | 事务与事务之间不会互相影响，一个事务的中间状态不会被其他事务感知。
持久性 | 单事务完成，那么事务对数据所做的变更就完全保存在数据库中，即使发生停电，系统宕机也是如此。

# 4. 应用场景

## 4.1. 支付

最经典场景就是支付，一笔支付，是对买家账户进行扣款，同时对卖家账户进行加钱，这些操作必须在一个事务里执行，要么全部成功，要么全部失败。而对于买家账户属于买家中心，对应的是买家数据库，而卖家账户属于卖家中心，对应的是卖家数据库，对不同数据库的操作必然需要引入分布式事务。

## 4.2. 在线订单

买家在电商平台下单，往往会涉及到两个动作，一个是扣库存，第二个是更新订单状态，库存和订单一般属于不同的数据库，需要使用分布式事务保证数据一致性。

# 5. 行业中常见解决方案

## 5.1. 本地消息表（异步确保）

本地消息表这种实现方式应该是业界使用最多的，其核心思想是将分布式事务拆分成本地事务进行处理，这种思路是来源于ebay。

基本思路：
~~~
消息生产方，需要额外建一个消息表，并记录消息发送状态。消息表和业务数据要在一个事务里提交，也就是说他们要在一个数据库里面。然后消息会经过MQ发送到消息的消费方。如果消息发送失败，会进行重试发送。

消息消费方，需要处理这个消息，并完成自己的业务逻辑。此时如果本地事务处理成功，表明已经处理成功了，如果处理失败，那么就会重试执行。如果是业务上面的失败，可以给生产方发送一个业务补偿消息，通知生产方进行回滚等操作。

生产方和消费方定时扫描本地消息表，把还没处理完成的消息或者失败的消息再发送一遍。如果有靠谱的自动对账补账逻辑，这种方案还是非常实用的。
~~~

特点：生产方和消费方定时扫描本地消息表，把还没处理完成的消息或者失败的消息再发送一遍。如果有靠谱的自动对账补账逻辑，这种方案还是非常实用的。

## 5.2. 两阶段提交

XA是X/Open CAE Specification (Distributed Transaction Processing)模型中定义的TM（Transaction Manager）与RM（Resource Manager）之间进行通信的接口。

两阶段提交是XA的标准实现。它将分布式事务的提交拆分为2个阶段：prepare和commit/rollback。

在XA规范中，数据库充当RM角色，应用需要充当TM的角色，即生成全局的txId，调用XAResource接口，把多个本地事务协调为全局统一的分布式事务。

XA中有两个重要的概念：事务管理器和本地资源管理器。其中本地资源管理器往往由数据库实现，比如Oracle、DB2这些商业数据库都实现了XA接口，而事务管理器作为全局的调度者，负责各个本地资源的提交和回滚。XA实现分布式事务的原理如下：

![XA原理图](https://i.loli.net/2019/11/15/uqbLIm5W8zywrtd.png)

特点：XA协议比较简单，目前很多商业数据库实现XA协议，使用分布式事务的成本也比较低。但是，XA也有致命的缺点，那就是性能不理想，特别是在交易下单链路，往往并发量很高，XA无法满足高并发场景。XA目前在商业数据库支持的比较理想，在mysql数据库中支持的不太理想，mysql的XA实现，没有记录prepare阶段日志，主备切换回导致主库与备库数据不一致。许多nosql也没有支持XA，这让XA的应用场景变得非常狭隘。在prepare阶段需要等待所有参与子事务的反馈，因此可能造成数据库资源锁定时间过长，不适合并发高以及子事务生命周长较长的业务场景。两阶段提交这种解决方案属于牺牲了一部分可用性来换取的一致性。

## 5.3. 事务消息+最终一致性

事务消息作为一种异步确保型事务， 将两个事务分支通过MQ进行异步解耦，事务消息的设计流程同样借鉴了两阶段提交理论。

- 事务发起方首先发送prepare消息到MQ。

- 在发送prepare消息成功后执行本地事务。

- 根据本地事务执行结果返回commit或者是rollback。

- 如果消息是rollback，MQ将删除该prepare消息不进行下发，如果是commit消息，MQ将会把这个消息发送给consumer端。

- 如果执行本地事务过程中，执行端挂掉，或者超时，MQ将会不停的询问其同组的其它producer来获取状态。

- Consumer端的消费成功机制有MQ保证。

基于消息中间件的两阶段提交往往用在高并发场景下，将一个分布式事务拆成一个消息事务（A系统的本地操作+发消息）+B系统的本地操作，其中B系统的操作由消息驱动，只要消息事务成功，那么A操作一定成功，消息也一定发出来了，这时候B会收到消息去执行本地操作，如果本地操作失败，消息会重投，直到B操作成功，这样就变相地实现了A与B的分布式事务

![20191115163019.png](https://i.loli.net/2019/11/15/MqRdIyDzhKiO2YN.png)

特点：第三方的MQ是支持事务消息的，比如RocketMQ，但是市面上一些主流的MQ都是不支持事务消息的，比如 RabbitMQ 和 Kafka 都不支持。

## 5.4. 补偿事务（TCC）

TCC 其实就是采用的补偿机制，其核心思想是：针对每个操作，都要注册一个与其对应的确认和补偿（撤销）操作。TCC模型是把锁的粒度完全交给业务处理。它分为三个阶段：

- Try 阶段主要是对业务系统做检测及资源预留

- Confirm 阶段主要是对业务系统做确认提交，Try阶段执行成功并开始执行 Confirm阶段时，默认 Confirm阶段是不会出错的。即：只要Try成功，Confirm一定成功。

- Cancel 阶段主要是在业务执行错误，需要回滚的状态下执行的业务取消，预留资源释放。

特点：TCC模型对业务的侵入强，改造的难度大。


# 6. SpringBoot整合Atomikos

## 6.1. 环境概述

- 开发环境：Maven+IDEA

- 组件版本

> SpringBoot版本：2.1.8.RELEASE

> mybatis-spring-boot-starter： 1.3.4

> druid-spring-boot-starter： 1.1.13

> mysql-connector-java： 5.1.40

## 6.2. 添加依赖

在POM文件中添加springboot集成atomikos的依赖，已经帮我们集成好transaction-jms、transaction-jta、transaction-jdbc、javax.transaction-api

~~~
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jta-atomikos</artifactId>
</dependency>
~~~

## 6.3. application文件配置

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

## 6.4. SQL脚本

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

## 6.5. 数据源核心配置

既然两套不通数据源，我们就应该有两套不通数据源的配置、注册、事务管理等，因为我使用Druid，所以这里就演示用Druid的配置，其他配置，大家可自行在网上找材料，也不是很难。

以下为核心代码

### 6.5.1. DruidConfig

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

### 6.5.2. 数据源A的配置

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

### 6.5.3. 数据源B的配置

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

### 6.5.4. 注意

在演示样例中只使用一个事务管理器：xatx，并没有使用TxAdviceInterceptor.java和TxAdvice2Interceptor.java中配置的事务管理器；有需求的童鞋可以自己配置其他的事务管理器；(见DruidConfig.java中查看) 

## 6.6. 编写样例

### 6.6.1. 服务层

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

### 6.6.2. 利用SpringBoot中的JUnit单元测试

~~~
@Autowired
private JtaService jtaService;

@Test
public void testJTA(){
    jtaService.testJTA();
}

~~~

# 7. SpringBoot整合Bitronix

# 8. 源码

[Github演示源码]([https://github.com/king-angmar/weathertop/tree/master/akkad-springboot/springboot-dist-trans]) ，记得给Star