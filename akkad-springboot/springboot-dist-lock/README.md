
<!-- TOC -->

- [1. 概述](#1-概述)
- [2. 特点](#2-特点)
- [3. 实现方式](#3-实现方式)
- [4. 数据库实现](#4-数据库实现)
    - [4.1. 前提](#41-前提)
        - [4.1.1. 乐观锁](#411-乐观锁)
        - [4.1.2. 悲观锁](#412-悲观锁)
    - [4.2. 乐观锁实现](#42-乐观锁实现)
    - [4.3. 悲观锁实现](#43-悲观锁实现)
        - [4.3.1. 方案1_for update](#431-方案1_for-update)
        - [4.3.2. 方案2_唯一性约束](#432-方案2_唯一性约束)
        - [4.3.3. 方案3_查询并占有](#433-方案3_查询并占有)
        - [4.3.4. 附 表结构](#434-附-表结构)
    - [4.4. 总结](#44-总结)
        - [4.4.1. 缺点](#441-缺点)
- [5. Redis实现](#5-redis实现)
    - [5.1. redis单实例](#51-redis单实例)
    - [5.2. redis集群实现](#52-redis集群实现)
- [6. Zookeeper实现](#6-zookeeper实现)
    - [6.1. 引入POM依赖](#61-引入pom依赖)
    - [6.2. application文件](#62-application文件)
    - [6.3. 核心实现](#63-核心实现)
    - [6.4. 演示结果](#64-演示结果)
- [7. 三种锁的比较](#7-三种锁的比较)
- [8. 源码](#8-源码)

<!-- /TOC -->

# 1. 概述

在高可用大型互联网业务系统都是高度复杂且都是分布式架构，这样的确提高了可用性和和稳定性，但也带来也多节点在执行过程中相互干扰，这在针对同一资源的处理过程中产生数据不一致。如果能保证在同一时刻该资源只能被一个应用处理，而不能并发处理。由此我们引入一种分布式协调机制来调度，而这种分布式协调机制的核心我们称之为分布式锁。

![20191115223356.png](https://i.loli.net/2019/11/15/C58bJhagzF1wROp.png)

+ 成员变量 V 存在 JVM_1、JVM_2、JVM_3 三个 JVM 内存中
+ 成员变量 V 同时都会在JVM分配一块内存，三个请求发过来同时对这个变量操作，显然结果是不对的
+ 不是同时发过来，三个请求分别操作三个不同 JVM 内存区域的数据，变量 V 之间不存在共享，也不具有可见性，处理的结果也是不对的

注：该成员变量**V**是一个有状态的对象，主要体现在一个类的成员变量。

# 2. 特点

- 在分布式系统环境下，同一个方法在同一时间只能被一个机器的同一个线程执行；
- 高可用的获取锁与释放锁；
- 高性能的获取锁与释放锁；
- 具备可重入特性；
- 具备锁的自我失效机制，防止死锁；
- 满足非阻塞锁特性，即没有获取到锁将直接返回获取锁失败。

# 3. 实现方式

根据分布式的CAP理论我们了解“任何一个分布式系统都无法同时满足一致性（Consistency）、可用性（Availability）和分区容错性（Partition tolerance），最多只能同时满足两项。”

所以我们在系统设计之初就分析调研，根据分析调研结果对这三者做取舍。

借鉴在主流互联网的经验，都牺牲强一致性来换取系统的高可用性，系统的“一致性”只保证“最终一致性”，这个最终时间需要在可控可接受的范围内。很多场景下，我们为了保证最终一致性，都会做很多技术方案来支持，比如分布式事务、分布式锁。

在分布式锁的技术实现上，主流认可有三种实现方式，从复杂度来看，由难至易依次增加：
> 基于数据库实现分布式锁；

> 基于缓存（Redis/Memcached等）实现分布式锁；

> 基于Zookeeper实现分布式锁；

无论哪一种方式，都不可能完美，需要根据实际业务场景做出选择。

# 4. 数据库实现

## 4.1. 前提

在了解数据库实现分布式锁的之前，我们首了解**乐观锁（Optimistic Lock）**和**悲观锁（Pessimistic Lock）**。

### 4.1.1. 乐观锁

每次去取数据的时候都会认为不会有其他线程对数据进行修改，因此不会上锁，但是在更新时会判断其他线程在这之前有没有对数据进行修改，一般会使用版本号机制或CAS操作实现；乐观锁适用于多读的应用类型，这样可以提高吞吐量，像数据库如果提供类似于write_condition机制的其实都是提供的乐观锁。

### 4.1.2. 悲观锁

每次取数据时都认为其他线程会修改，所以都会加锁（读锁、写锁、行锁等），当其他线程想要访问数据时，都需要block阻塞挂起。可以依靠数据库实现，如行锁、读锁和写锁等，都是在操作之前加锁，它对数据被外界（包括本系统当前的其他事务，以及来自外部系统的事务处理）修改持保守态度，因此，在整个数据处理过程中，将数据处于锁定状态。悲观锁的实现，往往依靠数据库提供的锁机制（也只有数据库层提供的锁机制才能真正保证数据访问的排他性，否则，即使在本系统中实现了加锁机制，也无法保证外部系统不会修改数据）。在 Java中，synchronized的思想也是悲观锁。

**乐观锁、悲观锁**两种锁各有优缺点，不可认为一种好于另一种，像乐观锁适用于写比较少的情况下，即冲突真的很少发生的时候，这样可以省去了锁的开销，加大了系统的整个吞吐量。但如果经常产生冲突，上层应用会不断的进行retry，这样反倒是降低了性能，所以这种情况下用悲观锁就比较合适。本质上，数据库的乐观锁做法和悲观锁做法主要就是解决下面假设的场景，避免丢失更新问题：

## 4.2. 乐观锁实现

从上面的前提概要我们知道，乐观锁机制其实就是在数据库表中引入一个版本号（version）字段来实现的。

当我们要从数据库中读取数据的时候，同时把这个version字段也读出来，如果要对读出来的数据进行修改写回数据库时，则需要将version加1，同时将新的数据与新的version更新到数据表中，且必须在更新的时候同时检查目前数据库里version值是不是之前的那个version，如果是，则正常更新。如果不是，则更新失败，说明在这个过程中有其它的进程去更新过数据了。

![20191115230824.png](https://i.loli.net/2019/11/15/5oOsY7n8GQURB2r.png)

假定同一账号下，小明和小明媳妇同时取银行进行取款操作，账户余额200￥，小明取款100￥，小明媳妇取款150￥。

- 没有锁机制的场景下，可能会出现账户同时被扣款150￥和100￥，导致账户余额出现负数情况，这样的话银行可能混不下去。

- 如果我们用到锁机制，小明和小明媳妇都在取款时候，除看到余额的操作，还在后台事务中读取版本号version=1，不论小明和小明媳妇先成功执行取款操作，都会将版本号version+1，即version=2，没有成功执行的那一方再去操作时候读取出来的version是2，那么就会触发重新获取余额。

乐观锁关键点：

- 锁服务要有递增的版本号version

- 每次更新数据都要先判断版本号对不对，然后再写入新的版本号

## 4.3. 悲观锁实现

### 4.3.1. 方案1_for update

Oracle、Mysql中是基于<font color=red>**for update**</font>来实现加锁的。在MYSQL中需要注意的是，在InnoDB中只有字段加了索引的，才会是行级锁，否者是表级锁，所以一定要对where的条件字段加索引。

当这条记录加上排它锁之后，其它线程是无法操作这条记录的。

### 4.3.2. 方案2_唯一性约束

对method_name做了唯一性约束，这里如果有多个请求同时提交到数据库的话，数据库会保证只有一个操作可以成功。

~~~

INSERT INTO method_lock (method_name, desc) VALUES ('methodName', 'methodName');
~~~

### 4.3.3. 方案3_查询并占有

先获取锁的信息：

~~~
select id, method_name, state,version from method_lock where state=1 and method_name='methodName';
~~~

再占有

~~~

update t_resoure set state=2, version=2, update_time=now() where method_name='methodName' and state=1 and version=2;

~~~

如果没有更新影响到一行数据，则说明这个资源已经被别人占位了。

### 4.3.4. 附 表结构

~~~

DROP TABLE IF EXISTS method_lock;

CREATE TABLE method_lock (
lck_id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
method_name VARCHAR(64) NOT NULL COMMENT '锁定的方法名',
state TINYINT NOT NULL COMMENT '1:未分配；2：已分配',
update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ver INT NOT NULL COMMENT '版本号',
PRIMARY KEY (lck_id),
UNIQUE KEY uidx_method_name (method_name) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='锁定中的方法';
~~~

## 4.4. 总结

### 4.4.1. 缺点

> 这把锁强依赖数据库的可用性，数据库是一个单点，一旦数据库挂掉，会导致业务系统不可用。

> 这把锁没有失效时间，一旦解锁操作失败，就会导致锁记录一直在数据库中，其他线程无法再获得到锁。

> 这把锁只能是非阻塞的，因为数据的insert操作，一旦插入失败就会直接报错。没有获得锁的线程并不会进入排队队列，要想再次获得锁就要再次触发获得锁操作。

> 这把锁是非重入的，同一个线程在没有释放锁之前无法再次获得该锁。因为数据中数据已经存在了。

配置实现细节

- 数据库是单点？搞两个数据库，数据之前双向同步。一旦挂掉快速切换到备库上。

- 没有失效时间？只要做一个定时任务，每隔一定时间把数据库中的超时数据清理一遍。

- 非阻塞的？搞一个while循环，直到insert成功再返回成功。

- 非重入的？在数据库表中加个字段，记录当前获得锁的机器的主机信息和线程信息，那么下次再获取锁的时候先查询数据库，如果当前机器的主机信息和线程信息在数据库可以查到的话，直接把锁分配给他就可以了。

# 5. Redis实现

基于Redis实现的锁机制，主要是依赖redis自身的原子操作，因为redis是单线程。要求redis版本大于2.6.12。

## 5.1. redis单实例

- 引入pom

版本号大家以实际使用中的为准，我这里仅供参考，因为spring-boot的自动注册功能会为我们提供StringRedisTemplate，直接使用即可。

~~~
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
~~~

- yml文件

~~~
server:
  port: 9090

spring:
  datasource:
    name: mysql
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/springboot?characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 30000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: select 1
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: false
      max-pool-prepared-statement-per-connection-size: 20
      connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=6000

  redis:
    database: 0
    host: 127.0.0.1
    port: 6379

mybatis:
  mapperLocations: classpath:mapper/**/*.xml
~~~

- 核心实现

~~~

package xyz.wongs.weathertop.comp;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockComponent {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * @Description 获取锁，默认：失效时间为5，失效时间的单位为秒，重试次数为3，休眠4秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value){
        return getLock(key,value,5, TimeUnit.SECONDS,3,5000);
    }

    /**
     * @Description 获取锁，默认：失效时间的单位为秒，重试次数为3，休眠4秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire){
        return getLock(key,value,expire, TimeUnit.SECONDS,3,5000);
    }

    /**
     * @Description 获取锁，默认重试次数为3，休眠5秒
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @param unit
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire, TimeUnit unit){
        return getLock(key,value,expire, unit,3,5000);
    }

    /**
     * @Description
     * @param key
     * @param value
     * @param expire    redis过期时间
     * @param unit
     * @param tryCount  重试次数
     * @param waitMillis 每次重试要等待时间
     * @return boolean
     * @throws
     * @date 2019/11/16 21:37
     */
    public boolean getLock(String key,String value,long expire, TimeUnit unit,int tryCount,int waitMillis){
        //setIfAbsent如果键不存在则新增,存在则不改变已经有的值。
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key,value,expire,unit);
        if(success) {
            return true;
        }
        //判断锁超时 - 防止原来的操作异常，没有运行解锁操作  防止死锁
        String val = stringRedisTemplate.opsForValue().get(key);
        if(!Strings.isNullOrEmpty(val)){
            if(System.currentTimeMillis() - Long.parseLong(val) > unit.toMillis(expire)){
                // 超时移除
                stringRedisTemplate.delete(key);
            }
        }
        // 重试、等待
        if (tryCount > 0 && waitMillis > 0) {
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                log.error("getLock exception{}",e.getMessage());
            }
            return getLock(key,value,expire,unit,tryCount - 1,waitMillis);
        }
        return false;
    }

    /**
     * @Description 获取等待时间
     * @param key
     * @param expire
     * @param unit
     * @return long 秒
     * @throws
     * @date 2019/11/16 21:44
     */
    public long getWaitSecond(String key,long expire,TimeUnit unit) {
        long currentTime = System.currentTimeMillis();
        long preTime = Long.parseLong(stringRedisTemplate.opsForValue().get(key));
        return (preTime + unit.toMillis(expire) - currentTime) / 1000;
    }


    /**
     * @Description 设置锁的过期时间，默认单位为毫秒
     * @param key
     * @param expTime
     * @return Boolean
     * @throws
     * @date 2019/11/16 21:18
     */
    public Boolean renewal(String key,int expTime){
        return renewal(key, expTime, TimeUnit.MILLISECONDS);
    }

    /**
     * @Description 设置锁的过期时间
     * @param key
     * @param expTime
     * @param unit
     * @return Boolean
     * @throws
     * @date 2019/11/16 21:18
     */
    public Boolean renewal(String key,int expTime,TimeUnit unit){
        return stringRedisTemplate.expire(key, expTime, unit);
    }


    /**
     * @Description 解锁
     * @param key
     * @param val
     * @return void
     * @throws
     * @date 2019/11/16 21:13
     */
    public void unlock(String key,String val){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            if(!Strings.isNullOrEmpty(value) && val.equals(value) ){
                // 删除锁状态
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("unlock exception{}",e);
        }
    }

}

~~~

- 模拟样例

~~~

package xyz.wongs.weathertop.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.comp.RedisLockComponent;
import xyz.wongs.weathertop.deno.entity.RedisLock;
import xyz.wongs.weathertop.deno.mapper.RedisLockMapper;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class RedisController {

    @Autowired
    private RedisLockMapper redisLockMapper;

    @Autowired
    private RedisLockComponent redisLockComponent;

    /**
     * 超时时间 5s
     */
    private static final int TIMEOUT = 3;

    @RequestMapping(value = "/seckilling/{key}")
    public ResponseResult Seckilling(@PathVariable("key") String key){
        ResponseResult responseResult = new ResponseResult();
        //1、加锁
        String value = System.currentTimeMillis() + "";
        if(!redisLockComponent.getLock(key,value,6)){
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.DICT_LOCK_FAIL.getCode());
            responseResult.setMsg("排队人数太多，请稍后再试.");
            return responseResult;
        }

        RedisLock redisLock = redisLockMapper.selectByPrimaryKey(1);
        // 2、查询库存，为0则活动结束
        if(redisLock.getCounts()==0){
            responseResult.setStatus(false);
            responseResult.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
            responseResult.setMsg("库存不够.");
            return responseResult;
        }
        //3、减库存
        redisLock.setCounts(redisLock.getCounts()-1);
        redisLockMapper.updateByPrimaryKeySelective(redisLock);
        try{
            Thread.sleep(5000);//模拟减库存的处理时间
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //4、释放锁
        redisLockComponent.unlock(key,value);
        responseResult.setMsg("恭喜您，秒杀成功。");
        return responseResult;
    }
}

~~~

![秒杀成功](https://i.loli.net/2019/11/16/iVX5OytkCSMbjxc.png)

## 5.2. redis集群实现

实际上我们为了高可用，降低单机故障概率，肯定将redis集群模式部署，这样情况下我们整合redisson，由于篇幅有限，这里暂时挖个坑，就不探讨这个，下一篇再说

# 6. Zookeeper实现

在上一章节，我们演示基于Redis的实现，这一章节我们来看另一种分布式锁的实现——Zookeeper。

关于Zookeeper的为什么能实现分布式锁，这里只说明zookeeper核心保存结构是DataTree数据结构，内部实现基于Map<String, DataNode>的数据结构，其他详细内容大家自行取官网查阅，这里也不赘述。
[Zookeeper官方描述](https://zookeeper.apache.org)

![20191118155851.png](https://i.loli.net/2019/11/18/DgBHOPWLJTZdnYk.png)

Zookeeper中提供了节点类型主要有:

- 持久节点：节点创建后，将一直存在，直到有删除操作来主动清除。

- 顺序节点：假如当前一个父节点为/lock，可以在该父节点下面创建子节点；Zookeeper本身提供了可选的有序特性，例如我们可以创建子节点“/lock/test_”并且指明有序，那么Zookeeper在生成子节点时会根据当前子节点数量自动添加整数序号，如果第一个子节点为/lock/test_0000000000，下一个节点则为/lock/test_0000000001，依次类推。

- 临时节点：客户端可以建立一个临时节点，在会话结束或者会话超时后，zookeeper会自动删除该节点。

Zookeeper从诞生至今，也有基于zk的分布式锁的实现有成熟的框架，这里Curator就是代表，它就是Netflix开源的一套ZooKeeper客户端框架，它提供了zk场景的绝大部分实现，使用Curator就不必关心其内部算法，Curator提供了来实现分布式锁，用方法获取锁，以及用方法释放锁，同其他锁一样，方法需要放在finakky代码块中，确保锁能正确释放。

Curator提供了四种分布式锁：

+ InterProcessMutex：分布式可重入排它锁

+ InterProcessSemaphoreMutex：分布式排它锁

+ InterProcessReadWriteLock：分布式读写锁

+ InterProcessMultiLock：将多个锁作为单个实体管理的容器

说了这么多，Zookeeper如何实现分布式锁，接下来代码奉上，我们以Springboot载体，写一个案例。

## 6.1. 引入POM依赖

~~~
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.10</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </exclusion>
        <exclusion>
            <artifactId>log4j</artifactId>
            <groupId>log4j</groupId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>

~~~

## 6.2. application文件

~~~
curator:
  retryCount: 5
  elapsedTimeMs: 5000
  connectString: 192.168.147.132:2181
  # session超时时间
  sessionTimeoutMs: 60000
  # 连接超时时间
  connectionTimeoutMs: 5000
~~~

## 6.3. 核心实现

~~~
/**
    * @Description 获取分布式锁
    * @param path 提供可供写入的路径
    * @return void
    * @throws
    * @date 2019/11/4 9:36
    */
public boolean acquireDistributedLock(String path) {

    boolean lock = true;
    String keyPath = "/" + ROOT_PATH_LOCK + "/" + path;
    while (true) {
        try {
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(keyPath);
            log.info("success to acquire lock for path:{}", keyPath);
            break;
        } catch (Exception e) {
            log.info("failed to acquire lock for path:{}", keyPath);
            log.info("while try again .......");
            try {
                if (countDownLatch.getCount() <= 0) {
                    countDownLatch = new CountDownLatch(1);
                }
                countDownLatch.await();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            lock = false;
        }
    }
    return lock;
}


/**
    * @Description
    * @param path  释放分布式锁
    * @return boolean
    * @throws
    * @date 2019/11/4 9:36
    */
public boolean releaseDistributedLock(String path) {
    boolean release = true;
    String keyPath = "/" + ROOT_PATH_LOCK + "/" + path;;
    try {
        if (curatorFramework.checkExists().forPath(keyPath) != null) {
            curatorFramework.delete().forPath(keyPath);
        }
    } catch (Exception e) {
        log.error("failed to release lock");
        release = false;
    }
    return release;
}
~~~

## 6.4. 演示结果

我用webcontroller实现一个restfull接口，同时打开两个URL获取同一把锁，获取的为成功，否则失败。

~~~

@GetMapping("/lock10")
public ResponseResult getLock1() {
    ResponseResult responseResult = new ResponseResult();
    Boolean acquire = distributedLockByZookeeper.acquireDistributedLock(PATH);
    try {
        if(acquire) {
            log.error("I am lock1，i am updating resource……！！！");
            Thread.sleep(2000);
        } else{
            responseResult.setCode(ResponseCode.SYSNC_LOCK.getCode());
            responseResult.setMsg(ResponseCode.SYSNC_LOCK.getMsg());
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    } finally {
        distributedLockByZookeeper.releaseDistributedLock(PATH);
    }
    return responseResult;
}
~~~

![获取锁成功](https://i.loli.net/2019/11/18/cpi5JFdMblRX7Dj.png)

![未抢到锁](https://i.loli.net/2019/11/18/xC5Q7spcRmwXWbA.png)

# 7. 三种锁的比较

这三种方式都不是尽善尽美，如同CAP，在复杂性、可靠性、性能等方面皆无法同时满足，所以，根据实际业务情况选择最适合的方案，优雅的解决问题，少带来弊端即可。

- 实现难易程度自低至高：数据库 > 缓存 > Zookeeper

- 实现的复杂性自低至高：Zookeeper >= 缓存 > 数据库

- 从性能角度自高到低：缓存 > Zookeeper >= 数据库

- 从可靠性角度自高到低：Zookeeper > 缓存 > 数据库

在技术层面Redis是nosql数据，而Zookeeper是分布式协调工具，都用于分布式解决方案；防死锁的实现上Redis是通过对key设置有效期来解决死锁，而Zookeeper使用会话有效期方式解决死锁现象；数据库的实现上，高并发过程中对库的压力会增大

# 8. 源码

[Github演示源码]([https://github.com/king-angmar/weathertop/tree/master/akkad-springboot/springboot-dist-lock]) ，记得给Star。