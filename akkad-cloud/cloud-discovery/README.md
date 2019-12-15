# 1. [CentOS环境下安装Nacos](https://www.jianshu.com/p/9f695cf38cf3)

# 2. [SpringCloud集成Nacos实现配置管理](https://www.jianshu.com/p/f8cf6bb36107)

# 3. 服务发现

本章节我通过在SpringCloud中写服务者（Provider，端口：9001）、消费者(Consumer，端口：9002)，来演示服务发现。

![服务发现原理](https://i.loli.net/2019/12/15/9JWHmN1iv7u2Pck.png)

服务发现添加依赖
~~~
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
~~~

## 3.1. 服务提供者-Provider

编写这个，我们基于
[SpringCloud集成Nacos实现配置管理](https://www.jianshu.com/p/f8cf6bb36107) 中的案例来改造

### 3.1.1. 添加POM依赖包

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

### 3.1.2. resources文件夹

#### 3.1.2.1. bootstrap.properties文件

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

#### 3.1.2.2. application.yml文件

~~~
server:
  port: 9001

mybatis:
  mapperLocations: classpath:mapper/**/*.xml
~~~

### 3.1.3. 新增配置项

将Data Id中cofig-nacos-dev.yml配置复制一份，重命名为discovery-nacos-provider-dev.yml。

![discovery-nacos-provider-dev.yml](https://i.loli.net/2019/12/15/CQiqFLJm3NU7A1B.png)

### 3.1.4. 应用入口

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

