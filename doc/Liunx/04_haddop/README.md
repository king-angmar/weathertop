
环境须知：
- CentOS7
- hadoop-3.1.2.tar.gz
- jdk8

Hadoop环境需要JAVA环境，所以首先得安装Java。

## 11.1. 伪分布式环境部署

### 11.1.1. 创建用户组、用户

~~~
[root@localhost app]$  groupadd dev
[root@localhost app]$  adduser hadoop
[root@localhost app]$  passwd hadoop
~~~

### 11.1.2. ssh安装配置免密登陆

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

### 11.1.3. hadoop安装

#### 11.1.3.1. 下载

[Hadoop下载](https://mirrors.cnnic.cn/apache/hadoop/common/hadoop-3.1.2/hadoop-3.1.2.tar.gz)

~~~
[hadoop@localhost hadoop]$  wget https://mirrors.cnnic.cn/apache/hadoop/common/hadoop-3.1.2/hadoop-3.1.2.tar.gz

[hadoop@localhost hadoop]$  tar zxvf hadoop-3.1.2.tar.gz

[hadoop@localhost hadoop]$  mv hadoop-3.1.2/ /data/app/hadoop/
~~~

#### 11.1.3.2. 配置

- etc/hadoop/core-site.xml，configuration配置为
~~~
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
~~~

- 设置环境变量

~~~
export HADOOP_HOME=/data/app/hadoop/
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop/
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native/
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"
~~~

~~~
[hadoop@localhost hadoop]$  vi ~/.bashrc
[hadoop@localhost hadoop]$  source ~/.bashrc
~~~

- 设置JAVA_HOME
etc/hadoop/hadoop-env.sh
~~~
export JAVA_HOME=/data/app/jdk8
~~~

- 初始化 格式化HDFS

~~~
[hadoop@localhost hadoop]$ pwd
/data/app/hadoop
[hadoop@localhost hadoop]$ ./bin/hdfs namenode -format

~~~

- 启动NameNode和DataNode

~~~
[hadoop@localhost hadoop]$ ./sbin/start-dfs.sh
~~~

<font color=red>**输入地址查看：**<font/>http://192.168.147.132:9870/

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
[hadoop@localhost hadoop]$ ./sbin/start-yarn.sh
~~~

<font color=red>**输入地址查看：**<font/>http://192.168.147.132:8088/cluster
![查看YARN](doc/image/hadoop/2.png)

- 启动与停止
~~~
[hadoop@localhost hadoop]$ ./sbin/start-dfs.sh
[hadoop@localhost hadoop]$ ./sbin/start-yarn.sh

[hadoop@localhost hadoop]$ ./sbin/stop-dfs.sh
[hadoop@localhost hadoop]$ ./sbin/stop-yarn.sh
~~~

### 11.1.4. HBase安装

#### 11.1.4.1. 单机HBase配置

- conf/hbase-site.xml，configuration配置为

~~~
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://localhost:9012/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/hadoop/zookeeper</value>
  </property>
</configuration>
~~~

- 启动与关闭
~~~
[hadoop@localhost hbase]$   ./bin/start-hbase.sh

[hadoop@localhost hbase]$   ./bin/stop-hbase.sh
~~~

- jps查看

![查看NameNode](doc/image/hbase/1.png)

- 终端

~~~
[hadoop@localhost hbase]$   ./bin/hbase shell
~~~

- 禁用自带Zookeeper
conf/hbase-env.sh
~~~
[hadoop@localhost hbase]$   vi conf/hbase-env.sh
~~~

![查看NameNode](doc/image/hbase/2.png)

- 配置独立Zookeeper

这是官方文档推荐的做法，如果不拷贝 zoo.cfg，在 hbase-site.xml 中也可以对Zookeeper进行相关配置，但HBase会优先使用 zoo.cfg（如果有的话）的配置
~~~
[hadoop@localhost hbase]$   cp ../zookeeper/conf/zoo.cfg conf/
~~~

- hbase-site.xml配置

~~~
<property>
     <name>hbase.cluster.distributed</name>
     <value>true</value>
</property>
~~~

- 启动Zookeeper

~~~
[hadoop@localhost zookeeper]$ sh bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /data/app/zookeeper/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
~~~

#### 11.1.4.2. 集群模式<待补充>

### 11.1.5. Phoenix安装

版本要与HBase相匹配！

### 11.1.6. hive安装

[Hive下载](https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz)

~~~
[hadoop@localhost hadoop]$  wget https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-3.1.2/apache-hive-3.1.2-bin.tar.gz

[hadoop@localhost hadoop]$  tar zxvf apache-hive-3.1.2-bin.tar.gz

[hadoop@localhost hadoop]$  mv apache-hive-3.1.2 /data/app/haddop/
~~~