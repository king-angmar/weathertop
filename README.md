<center><h1>weathertop</h1></center>

<h2>风云顶（Weathertop），指环王魔戒中一处地名，辛达语中称为阿蒙苏尔，是风云丘陵中最南端的山峰，它跟其他山岗稍稍分开。它顶端呈圆锥形，峰顶略显平坦。在指环王魔戒中放置了名为帕蓝提尔（Palantíri）的“真知晶石”，可用于远望和交流的一种黑色晶石。</h2>

<!-- TOC -->

- [1. 目录结构](#1-目录结构)
- [2. persistence-mybatis](#2-persistence-mybatis)
- [3. akkad-cloud入门](#3-akkad-cloud入门)
- [4. akkad-handball【手球】](#4-akkad-handball手球)
- [5. SpringBoot入门](#5-springboot入门)
- [6. Linux说明](#6-linux说明)
- [7. Kafka消息MQ](#7-kafka消息mq)
- [8. elasticsearch](#8-elasticsearch)
- [9. zookeeper安装](#9-zookeeper安装)
- [10. hadoop学习](#10-hadoop学习)

<!-- /TOC -->

# 1. 目录结构

~~~
|-- akkad-base                      ------------------------------基包
|   |-- base-persistence            ------------------------------持久层包
|   |   |-- persistence-domain      ------------------------------样例代码中对Locatin的DAO、MAPPER、MAPPING
|   |   |-- persistence-mybatis     ------------------------------Mybatis持久层基包
|   |-- base-utils                  ------------------------------通用工具包
|   |-- base-utils-non-db           ------------------------------无数据库依赖的工具包，pom
|-- akkad-cloud                     ------------------------------SpringCloud入门
|-- akkad-handball                  ------------------------------JAVA常用的学习样例
|-- akkad-springboot                ------------------------------Springboot集成大全
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

详情[可查看](akkad-cloud/README.md)

# 4. akkad-handball【手球】

# 5. SpringBoot入门

点击即可查看[Springboot](akkad-springboot/README.md)详细入门样例介绍

# 6. Linux说明

点击即可查看[Linux入门](doc/Liunx/Linux基本命令.md)详细入门样例介绍

# 7. Kafka消息MQ

点击即可查看[Kafka入门](doc/Liunx/02_kafka/README.md)详细入门样例介绍

# 8. elasticsearch

[详细查看elastic入门](akkad-springboot/springboot-elasticsearch/elastic.md)，介绍了安装，以及集成SpringBoot

# 9. zookeeper安装

点击即可查看[zookeeper入门](doc/Liunx/03_zookeeper/README.md)详细入门样例介绍

# 10. hadoop学习

点击即可查看[Hadoop学习](doc/Liunx/04_haddop/README.md)详细入门样例介绍