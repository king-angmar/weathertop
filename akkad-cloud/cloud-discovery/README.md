前情内容

[CentOS环境下安装Nacos](https://www.jianshu.com/p/9f695cf38cf3)
[SpringCloud集成Nacos实现配置管理](https://www.jianshu.com/p/f8cf6bb36107)

# 1. 服务发现

本章节我通过在SpringCloud中写服务者（Provider，端口：9001）、消费者(Consumer，端口：9002)，来演示服务发现。

![服务发现原理](https://i.loli.net/2019/12/15/9JWHmN1iv7u2Pck.png)

服务中消费者、服务提供者都需要添加这个依赖：
~~~
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
~~~

## 1.1. 服务提供者-Provider

编写这个，我们基于
[SpringCloud集成Nacos实现配置管理](https://www.jianshu.com/p/f8cf6bb36107) 中的案例来改造

### 1.1.1. 添加POM依赖包

~~~
<dependencies>
    <!--通用包依赖，一些Entity、Dao等-->
    <dependency>
        <groupId>xyz.wongs.weathertop</groupId>
        <artifactId>persistence-domain</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!--Nacos配置中心-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!--步骤1、这是新增：Nacos服务发现-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
</dependencies>
~~~

### 1.1.2. resources文件夹

#### 1.1.2.1. bootstrap.properties文件

~~~
//步骤2、服务名改一下
spring.application.name=discovery-nacos-provider

spring.profiles.active=dev
spring.cloud.nacos.config.group=CLOUD_GROUP
spring.cloud.nacos.config.server-addr=192.168.147.132:8848
spring.cloud.nacos.config.prefix=${spring.application.name}
spring.cloud.nacos.config.file-extension=yml
management.endpoints.web.exposure.include=*
//步骤3、新增配置
spring.cloud.nacos.discovery.server-addr=192.168.147.132:8848

~~~

#### 1.1.2.2. application.yml文件

这里设定端口9001

~~~
server:
  port: 9001

mybatis:
  mapperLocations: classpath:mapper/**/*.xml
~~~

### 1.1.3. 新增配置项

将Data Id中cofig-nacos-dev.yml配置复制一份，重命名为discovery-nacos-provider-dev.yml。

![discovery-nacos-provider-dev.yml](https://i.loli.net/2019/12/15/CQiqFLJm3NU7A1B.png)

### 1.1.4. 应用入口

~~~
@RefreshScope
@MapperScan(basePackages = {"xyz.wongs.weathertop.**.mapper"})
@EnableDiscoveryClient
@SpringBootApplication
public class DiscoveryProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryProviderApp.class,args);
    }
}
~~~

### 1.1.5. 启动项目

在浏览器打开地址：http://localhost:9001/locations/0 

![验证服务](https://i.loli.net/2019/12/15/B4jbGTJ3A6r5HKa.png)

生产者启动成功

再打开Nacos控制台“服务管理->服务列表”，有**discovery-nacos-provider**这个服务名，说明服务已经注册到Nacos。

![服务提供者](https://i.loli.net/2019/12/15/76d4LhoMTK8mQCl.png)

## 1.2. 源码

[消费者Github演示源码](https://github.com/king-angmar/weathertop/tree/master/akkad-cloud/cloud-discovery/discovery-nacos-provider) ，记得给Star

# 2. 服务消费者

## 2.1. resources文件夹

### 2.1.1. bootstrap.properties

~~~
spring.application.name=discovery-nacos-consumer
spring.profiles.active=dev
spring.cloud.nacos.discovery.server-addr=192.168.147.132:8848
management.endpoints.web.exposure.include=*
~~~

### 2.1.2. application.yml

我们设定端口9002

~~~
server:
  port: 9002
~~~

## 2.2. Bean注册

我们利用SpringBoot管理Bean的便捷性，来对RestTemplate实例进行管理，利用它的**LoadBalanced**注解来实现负载均衡。

~~~
@EnableDiscoveryClient
@SpringBootApplication
public class DiscoveryConsumerrApp {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryConsumerrApp.class,args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
~~~

## 2.3. 调用生产者

~~~
@RestController
@RequestMapping(value = "/locations")
public class LocationController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/consumer/{lv}")
    public ResponseResult echo(@PathVariable(value = "lv") Integer lv) {
        return restTemplate.getForObject("http://discovery-nacos-provider/locations/" + lv, ResponseResult.class);
    }
}
~~~

restTemplate.getForObject(String url, Class<T> responseType, Object... uriVariables)

- url：地址名，这里一定要填写Nacos服务列表中的服务名
- responseType： 返回数据类型，由于我使用了统一类型来返回数据，这里大家根据实际内容自行更改

## 2.4. 验证服务

打开浏览器，输入我们的地址：http://localhost:9002/locations/consumer/0 ，
控制台中日志有刷新：
~~~
2019-12-15 18:19:53.140  INFO 22048 --- [tp2060799061-34] c.n.l.DynamicServerListLoadBalancer      : DynamicServerListLoadBalancer for client discovery-nacos-provider initialized: DynamicServerListLoadBalancer:{NFLoadBalancer:name=discovery-nacos-provider,current list of Servers=[192.168.68.235:9001],Load balancer stats=Zone stats: {unknown=[Zone:unknown;	Instance count:1;	Active connections count: 0;	Circuit breaker tripped count: 0;	Active connections per server: 0.0;]
},Server stats: [[Server:192.168.68.235:9001;	Zone:UNKNOWN;	Total Requests:0;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Thu Jan 01 08:00:00 CST 1970;	First connection made: Thu Jan 01 08:00:00 CST 1970;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:0.0;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:0.0;	max resp time:0.0;	stddev resp time:0.0]
]}ServerList:org.springframework.cloud.alibaba.nacos.ribbon.NacosServerList@66588ff
2019-12-15 18:19:54.121  INFO 22048 --- [erListUpdater-0] c.netflix.config.ChainedDynamicProperty  : Flipping property: discovery-nacos-provider.ribbon.ActiveConnectionsLimit to use NEXT property: niws.loadbalancer.availabilityFilteringRule.activeConnectionsLimit = 2147483647
~~~

![查看数据结果](https://i.loli.net/2019/12/15/pmvyoChTsV5XMOf.png)

## 2.5. 源码

[消费者Github演示源码](https://github.com/king-angmar/weathertop/tree/master/akkad-cloud/cloud-discovery/discovery-nacos-consumer) ，记得给Star