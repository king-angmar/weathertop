# 1. Nacos

## 1.1. 简介

![NACOS](https://i.loli.net/2019/12/08/29OwjrpdXBQTncl.png)

Nacos是阿里巴巴最新开源的项目，核心定位是“一个更易于帮助构建云原生应用的动态服务发现、配置和服务管理平台”，Nacos提供一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

项目地址：https://github.com/alibaba/nacos/releases。

Nacos提供四个主要功能：

- 服务发现和服务运行状况检查

 Nacos使服务易于注册并通过DNS或HTTP接口发现其他服务。 Nacos还提供服务的实时运行状况检查，以防止向不正常的主机或服务实例发送请求

- 动态配置管理

动态配置服务使您可以在所有环境中以集中和动态的方式管理所有服务的配置。Nacos消除了在更新配置时重新部署应用程序和服务的需求，这使配置更改更加有效和敏捷。

- 动态DNS服务

Nacos支持加权路由，使您更轻松地在数据中心内的生产环境中实施中间层负载平衡，灵活的路由策略，流控制和简单的DNS解析服务。它可帮助您轻松实现基于DNS的服务发现，并防止应用程序耦合到特定于供应商的服务发现API。

- 服务和元数据管理

Nacos提供了一个易于使用的服务仪表板，可帮助您管理服务元数据，配置，kubernetes DNS，服务运行状况和指标统计信息。

## 1.2. 安装部署

### 1.2.1. 部署模式

- 单机模式 - 用于测试和单机试用。
- 集群模式 - 用于生产环境，确保高可用。
- 多集群模式 - 用于多数据中心场景。

### 1.2.2. 环境准备

- JDK8+
- Mysql 5.6.5+

### 1.2.3. 下载安装文件

从官方网站下载最新安装 [latest stable release](https://github.com/alibaba/nacos/releases)。

这里以**nacos-server-1.1.4.tar.gz**作为演示版本。

下载完之后，解压文件，这块就没什么好说的，具体在什么目录下，自行定义。为了安全需要，还是创建一个用户**nacos**用于启停nacos服务。

~~~

[root@localhost app]$ pwd
/data/app/

[root@localhost app]$ adduser naocs -g:dev

[root@localhost app]$ passwd naocs

[root@localhost app]$ chown -R nacos:dev nacos/

[root@localhost app]$ cd naocs

[root@localhost naocs]$ su naocs

[naocs@localhost naocs]$ cd bin

~~~

### 1.2.4. 初始化数据库

虽然只有在集群模式下才会用到Msql，但是为了效果，依然在mysql新建一个实例nacos_config，执行初始化脚本<nacos-mysql.sql>，脚本在naocs安装文件夹**conf**下。

### 1.2.5. 修改配置

配置文件也在naocs安装文件夹**conf**下，文件名称**application.properties**，配置文件中提供修改默认端口、访问路径的属性等，我这为演示需要，只添加了数据库配置项，仅供参考。

~~~
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://localhost:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123456

~~~

### 1.2.6. 启动服务

独立模式服务运行比较简单，直接指定-m模式命令后的参数启动即可。

~~~
[naocs@localhost bin]$ sh startup.sh -m standalone
~~~

Windows平台中可以双击startup.cmd以运行NacosServer。

~~~
cmd startup.cmd -m standalone
~~~

![后台启动日志](https://i.loli.net/2019/12/08/zpf4MLbQRhd3nKT.png)


浏览器访问地址：http://ip:port:8848/nacos

输入默认账号密码：nacos/nacos，即可看到

![启动后效果](https://i.loli.net/2019/12/08/ZwJ51d7bQzBc2Oq.png)

单机独立模式启动成功！！！

在实际生产环境中Nacos肯定都是集群模式，一般而言，集群模式至少3个节点。

集群配置文件在安装文件夹**conf**下cluster.conf，将每个nacos节点按照IP:PORT格式配置即可，按行分割。

~~~
//IP:PORT
192.168.9.121:8848
192.168.9.122:8848
192.168.9.122:8848
~~~

启动命令**sh startup.sh**，不带任何参数。

使用Nacos集群需要利用负载均衡来代理每个节点，额外的配置非本篇重点介绍，大家自行完成。
