
## 8.1. Kafka

### 8.1.1. kafka安装

[官方kafka_2.12-2.3.0下载](https://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz)

<font color=red>**解压重命名等步骤略过，这些在Linux下通用操作，不懂问百度**</font>


### 8.1.2. 编写配置文件

修改/data/kafka/config/server.properties，主要有以下

~~~

host.name=192.168.147.132

broker.id=0
port=9092

listeners=PLAINTEXT://192.168.147.132:9092
advertised.listeners=PLAINTEXT://192.168.147.132:9092
log.dirs=/data/kafka/tmp/kafka-logs

zookeeper.connect=localhost:2181
~~~

### 8.1.3. 启动

方便的话，编写一个启动脚本，不然每次挨个启动Zookeeper和Kafka，甚是麻烦

kafkastart.sh

~~~

!/bin/sh

sh $zookeeper_home/bin/zkServer.sh start &

sleep 12

sh /data/app/kafka/bin/kafka-server-start.sh  /data/app/kafka/config/server.properties &

~~~

### 8.1.4. 创建Topic

~~~
sh kafka-topics.sh --create --zookeeper 192.168.147.129:2181 --replication-factor 1 --partitions 1 --topic wongs
~~~

![图片alt](doc/image/kafka/01-kafka-create-topic.png)

### 8.1.5. 查看Topic

~~~
sh kafka-topics.sh --list --zookeeper 192.168.147.129:2181
~~~

![图片alt](doc/image/kafka/02-kafka-list-topic.png)
