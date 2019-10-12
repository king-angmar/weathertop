<!-- TOC -->

- [1. 目录结构](#1-目录结构)
- [2. persistence-mybatis](#2-persistence-mybatis)
- [3. akkad-cloud入门](#3-akkad-cloud入门)
    - [3.1. Nacos配置中心](#31-nacos配置中心)
    - [3.2. Nacos注册中心](#32-nacos注册中心)
    - [3.3. 网关Gateway](#33-网关gateway)
    - [3.4. 熔断Hystream](#34-熔断hystream)
- [4. akkad-java](#4-akkad-java)
    - [4.1. 设计模式akkad-design](#41-设计模式akkad-design)
- [5. SpringBoot入门](#5-springboot入门)
    - [5.1. zookeeper安装](#51-zookeeper安装)
        - [5.1.1. 下载](#511-下载)
        - [5.1.2. 配置安装](#512-配置安装)
    - [5.2. 消息](#52-消息)
        - [5.2.1. Kafka](#521-kafka)
            - [5.2.1.1. kafka安装](#5211-kafka安装)
            - [5.2.1.2. 编写配置文件](#5212-编写配置文件)
            - [5.2.1.3. 启动](#5213-启动)
            - [5.2.1.4. 创建Topic](#5214-创建topic)
            - [5.2.1.5. 查看Topic](#5215-查看topic)
        - [5.2.2. 应用集成](#522-应用集成)
            - [5.2.2.1. 生产者](#5221-生产者)
            - [5.2.2.2. 消费者](#5222-消费者)
            - [5.2.2.3. 演示](#5223-演示)
- [6. hadoop学习](#6-hadoop学习)
    - [6.1. 伪分布式环境部署](#61-伪分布式环境部署)
        - [6.1.1. 创建用户组、用户](#611-创建用户组用户)
        - [6.1.2. ssh安装配置免密登陆](#612-ssh安装配置免密登陆)
        - [6.1.3. hadoop安装](#613-hadoop安装)
            - [6.1.3.1. 下载](#6131-下载)
            - [6.1.3.2. 配置](#6132-配置)
        - [6.1.4. hive安装](#614-hive安装)

<!-- /TOC -->

# 1. 目录结构

~~~


|-- akkad-base                      ------------------------------基包
|   |-- base-persistence            ------------------------------持久层包
|   |   |-- persistence-mybatis     ------------------------------Mybatis持久层基包
|   |-- base-utils                  ------------------------------通用工具包
|-- akkad-cloud                     ------------------------------SpringCloud入门
|   |-- cloud-config                ------------------------------基于Nacos实现的配置中心
|-- akkad-handball                  ------------------------------样例
|   |-- handball-location           ------------------------------爬虫获取统计局区域样例
|   |-- handball-tess4j             ------------------------------OCR图片识别
|-- akkad-java                      ------------------------------JAVA学习
|   |-- akkad-design                ------------------------------设计模式
|   |-- akkad-socket                ------------------------------Scoket编程
|   |   |-- socket-client           ------------------------------Client端
|   |   |-- socket-server           ------------------------------Server端
|   |-- akkad-thread                ------------------------------多线程
|-- akkad-job                       ------------------------------基于XXL-JOB实现调度中心
|   |-- job-sms                     ------------------------------调度中心样例
|-- akkad-springboot                ------------------------------SPRINGBOOT入门
|   |-- springboot-async            ------------------------------异步
|   |-- springboot-atomikos         ------------------------------分布式事务
|   |-- springboot-jwt              ------------------------------JWT认证
|   |-- springboot-mq               ------------------------------异步MQ
|   |   |-- mq-kafka                ------------------------------Kafka实现
|   |   |   |-- kafka-client        ------------------------------消费者
|   |   |   |-- kafka-server        ------------------------------生产者
|-- akkad-webmagic                  ------------------------------
|   |-- src
|   |-- target
|-- doc
|   |-- image
|-- README.md
~~~

# 2. persistence-mybatis

基于Mybatis组件并结合泛型实现**BaseMapper、BaseService**，子类继承该类，即可拥有通用的CURD，减少重复编码的麻烦，提高开发效率。

- 分页组件是继承开源**com.github.pagehelper**
~~~
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.3</version>
</dependency>
~~~

- 在应用中PageHelper.startPage即可，后面需要紧跟着持久层语句，否则分页失效

~~~
    public PageInfo<T> selectPage(PaginationInfo pgInfo, T t) {
        PageHelper.startPage(pgInfo.getPageNum(), pgInfo.getPageSize());
        List<T> lt = getMapper().getList(t);
        PageInfo<T> pageInfo = new PageInfo<T>(lt);
        return pageInfo;
    }
~~~

# 3. akkad-cloud入门

## 3.1. Nacos配置中心

Nacos 支持基于 DNS 和基于 RPC 的服务发现（可以作为springcloud的注册中心）、动态配置服务（可以做配置中心）、动态 DNS 服务。

`官方介绍`
~~~
Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您实现动态服务发现、服务配置管理、服务及流量管理。
Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构(例如微服务范式、云原生范式)的服务基础设施。
~~~

- pom依赖包

~~~
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>0.1.2</version>
</dependency>
~~~

- application.yml

~~~
nacos:
  config:
    server-addr: 123.206.118.219:8848
~~~

## 3.2. Nacos注册中心

## 3.3. 网关Gateway

## 3.4. 熔断Hystream

# 4. akkad-java

## 4.1. 设计模式akkad-design

点击即可查看[设计模式](akkad-java/akkad-design/Observer.md)详细介绍

# 5. SpringBoot入门

## 5.1. zookeeper安装

### 5.1.1. 下载

[官方zookeeper下载](https://zookeeper.apache.org/releases.html)，下载ZooKeeper，目前最新的稳定版本为 3.5.5 版本，用户可以自行选择一个速度较快的镜像来下载即可.

这边演示用的版本**zookeeper-3.4.13.tar.gz**

### 5.1.2. 配置安装

- `修改配置文件`

路径/data/zookeeper-3.4.13/conf/下的**zoo_sample.cfg** 改名为**zoo.cfg**

~~~

数据文件存放目录
dataDir=/data/zookeeper-3.4.13/data/tmp
日志存放目录
dataLogDir=/data/zookeeper-3.4.13/data/log

~~~

- `启动`

~~~
sh /data/zookeeper-3.4.13/bin/zkServer.sh start &
~~~

![图片alt](doc/image/zk/01-start.png)

## 5.2. 消息

### 5.2.1. Kafka

#### 5.2.1.1. kafka安装

[官方kafka_2.12-2.3.0下载](https://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz)

**解压重命名等步骤略过，这些在Linux下通用操作，不懂问百度**


#### 5.2.1.2. 编写配置文件

修改/data/kafka/config/server.properties，主要有以下

~~~
listeners=PLAINTEXT://192.168.147.129:9092
advertised.listeners=PLAINTEXT://192.168.147.129:9092
log.dirs=/data/kafka/tmp/kafka-logs

zookeeper.connect=localhost:2181
~~~

#### 5.2.1.3. 启动

方便的话，编写一个启动脚本，不然每次挨个启动Zookeeper和Kafka，甚是麻烦

kafkastart.sh

~~~

!/bin/sh

sh $zookeeper_home/bin/zkServer.sh start &

sleep 12

sh /data/kafka/bin/kafka-server-start.sh  /data/kafka/config/server.properties &

~~~

#### 5.2.1.4. 创建Topic

~~~
sh kafka-topics.sh --create --zookeeper 192.168.147.129:2181 --replication-factor 1 --partitions 1 --topic wongs
~~~

![图片alt](doc/image/kafka/01-kafka-create-topic.png)

#### 5.2.1.5. 查看Topic

~~~
sh kafka-topics.sh --list --zookeeper 192.168.147.129:2181
~~~

![图片alt](doc/image/kafka/02-kafka-list-topic.png)

### 5.2.2. 应用集成

加入依赖包

~~~
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
~~~

#### 5.2.2.1. 生产者

- application.yml

~~~
spring:
  kafka:
    bootstrap-servers: 192.168.147.129:9092
    producer:
      retries: 0
      batch-size: 16384
      buffer-memory: 33554432
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      properties:
        linger.ms: 1
~~~

- 发送消息

~~~
@RequestMapping("/kafka")
@RestController
public class TestKafkaProducerController {

    private Logger LOG = LoggerFactory.getLogger(TestKafkaProducerController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public Response send(){
        String topic = "wongs";
        User user = User.getAuther();
        Response response = new Response();
        response.setData(user);
        String data = JSONObject.toJSON(user).toString();
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOG.error("kafka sendMessage error, ex = {}, topic = {}, data = {}", ex, topic, data);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOG.info("kafka sendMessage success topic = {}, data = {}",topic, data);
            }
        });
        return response;
    }
}
~~~

#### 5.2.2.2. 消费者

- application.yml

~~~
spring:
  kafka:
    bootstrap-servers: 192.168.147.129:9092
    consumer:
      group-id: test-consumer-group
      enable-auto-commit: true
      auto-commit-interval: 1000ms
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      properties:
        session.timeout.ms: 15000
~~~

- 监听

~~~
@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(topics = {"wongs"})
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }
    }
}

~~~

#### 5.2.2.3. 演示

![MQ生产端](doc/image/kafka/04-kafka-server.png)

![MQ消费端](doc/image/kafka/03-kafka-client.png)

# 6. hadoop学习

环境须知：
- CentOS7
- hadoop-3.1.2.tar.gz
- jdk8

Hadoop环境需要JAVA环境，所以首先得安装Java。

## 6.1. 伪分布式环境部署

### 6.1.1. 创建用户组、用户

~~~
[root@localhost app]$  groupadd dev
[root@localhost app]$  adduser hadoop
[root@localhost app]$  passwd hadoop
~~~

### 6.1.2. ssh安装配置免密登陆

~~~
[root@localhost app]$  su hadoop
[hadoop@localhost hadoop]$ ssh-keygen -t rsa
[hadoop@localhost hadoop]$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
[hadoop@localhost hadoop]$ chmod 0600 ~/.ssh/authorized_keys
~~~

测试输入
~~~
[hadoop@localhost hadoop]$  ssh localhost
~~~



### 6.1.3. hadoop安装

#### 6.1.3.1. 下载

[Hadoop下载](https://mirrors.cnnic.cn/apache/hadoop/common/hadoop-3.1.2/hadoop-3.1.2.tar.gz)

~~~
[hadoop@localhost hadoop]$  wget https://mirrors.cnnic.cn/apache/hadoop/common/hadoop-3.1.2/hadoop-3.1.2.tar.gz

[hadoop@localhost hadoop]$  tar zxvf hadoop-3.1.2.tar.gz

[hadoop@localhost hadoop]$  mv hadoop-3.1.2 /data/app/hadoop
~~~

#### 6.1.3.2. 配置

- etc/hadoop/core-site.xml，configuration配置为
~~~
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
~~~

- 初始化 格式化HDFS

~~~
[hadoop@localhost hadoop312]$ pwd
/data/app/hadoop/hadoop312
[hadoop@localhost hadoop312]$ ./bin/hdfs namenode -format

~~~

- 启动NameNode和DataNode

~~~
[hadoop@localhost hadoop312]$ ./sbin/start-dfs.sh
~~~

<font color=red>**输入地址查看：**<font/>http://192.168.56.101:9870/

![查看NameNode](doc/image/hadoop/1.png)

- 配置YARN

编辑etc/hadoop/mapred-site.xml，configuration配置为

~~~
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
~~~

编辑etc/hadoop/yarn-site.xml，configuration配置为

~~~
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
~~~

- 启动YARN

~~~
[hadoop@localhost hadoop312]$ ./sbin/start-yarn.sh
~~~

<font color=red>**输入地址查看：**<font/>http://192.168.56.101:8088/cluster
![查看YARN](doc/image/hadoop/2.png)

- 启动与停止
~~~
[hadoop@localhost hadoop312]$ ./sbin/start-dfs.sh
[hadoop@localhost hadoop312]$ ./sbin/start-yarn.sh

[hadoop@localhost hadoop312]$ ./sbin/stop-dfs.sh
[hadoop@localhost hadoop312]$ ./sbin/stop-yarn.sh
~~~

### 6.1.4. hive安装

[Hive下载](https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz)

~~~
[hadoop@localhost hadoop]$  wget https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz

[hadoop@localhost hadoop]$  tar zxvf apache-hive-3.1.2-bin.tar.gz

[hadoop@localhost hadoop]$  mv apache-hive-3.1.2 /data/app/haddop/
~~~