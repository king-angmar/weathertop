<!-- TOC -->

- [1. 目录结构](#1-目录结构)
- [2. SpringBoot入门](#2-springboot入门)
    - [2.1. zookeeper安装](#21-zookeeper安装)
        - [2.1.1. 下载](#211-下载)
        - [2.1.2. 配置安装](#212-配置安装)
    - [2.2. 消息](#22-消息)
        - [2.2.1. Kafka](#221-kafka)
            - [2.2.1.1. kafka安装](#2211-kafka安装)
            - [2.2.1.2. 编写配置文件](#2212-编写配置文件)
            - [2.2.1.3. 启动](#2213-启动)
            - [2.2.1.4. 创建Topic](#2214-创建topic)
            - [2.2.1.5. 查看Topic](#2215-查看topic)
        - [2.2.2. 应用集成](#222-应用集成)
            - [2.2.2.1. 生产者](#2221-生产者)
            - [2.2.2.2. 消费者](#2222-消费者)
            - [2.2.2.3. 演示](#2223-演示)

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

# 2. SpringBoot入门

## 2.1. zookeeper安装

### 2.1.1. 下载
[官方zookeeper下载](https://zookeeper.apache.org/releases.html)，下载ZooKeeper，目前最新的稳定版本为 3.5.5 版本，用户可以自行选择一个速度较快的镜像来下载即可.

这边演示用的版本**zookeeper-3.4.13.tar.gz**

### 2.1.2. 配置安装
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

## 2.2. 消息

### 2.2.1. Kafka

#### 2.2.1.1. kafka安装
[官方kafka_2.12-2.3.0下载](https://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz)

**解压重命名等步骤略过，这些在Linux下通用操作，不懂问百度**


#### 2.2.1.2. 编写配置文件

修改/data/kafka/config/server.properties，主要有以下

~~~
listeners=PLAINTEXT://192.168.147.129:9092
advertised.listeners=PLAINTEXT://192.168.147.129:9092
log.dirs=/data/kafka/tmp/kafka-logs

zookeeper.connect=localhost:2181
~~~

#### 2.2.1.3. 启动

方便的话，编写一个启动脚本，不然每次挨个启动Zookeeper和Kafka，甚是麻烦

kafkastart.sh

~~~

!/bin/sh

sh $zookeeper_home/bin/zkServer.sh start &

sleep 12

sh /data/kafka/bin/kafka-server-start.sh  /data/kafka/config/server.properties &

~~~

#### 2.2.1.4. 创建Topic

~~~
sh kafka-topics.sh --create --zookeeper 192.168.147.129:2181 --replication-factor 1 --partitions 1 --topic wongs
~~~

![图片alt](doc/image/kafka/01-kafka-create-topic.png)

#### 2.2.1.5. 查看Topic

~~~
sh kafka-topics.sh --list --zookeeper 192.168.147.129:2181
~~~

![图片alt](doc/image/kafka/02-kafka-list-topic.png)

### 2.2.2. 应用集成

加入依赖包

~~~
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
~~~

#### 2.2.2.1. 生产者

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

#### 2.2.2.2. 消费者

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

#### 2.2.2.3. 演示

![MQ生产端](doc/image/kafka/04-kafka-server.png)

![MQ消费端](doc/image/kafka/03-kafka-client.png)

