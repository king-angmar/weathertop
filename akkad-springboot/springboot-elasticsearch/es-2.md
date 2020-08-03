# 1. 目录
<!-- TOC -->

- [1. 目录](#1-目录)
- [2. SpringBoot集成](#2-springboot集成)
    - [2.1. POM配置](#21-pom配置)
    - [2.2. yml配置](#22-yml配置)
    - [2.3. 核心操作类](#23-核心操作类)
- [3. 实战](#3-实战)
    - [3.1. 索引管理](#31-索引管理)
        - [3.1.1. 创建索引](#311-创建索引)
        - [3.1.2. 查看索引](#312-查看索引)
        - [3.1.3. 删除索引](#313-删除索引)
- [4. 源码](#4-源码)

<!-- /TOC -->

![20191205172258.png](https://i.loli.net/2019/12/05/lLXVdyOnWavqioM.png)

# 2. SpringBoot集成

开发工具，这里选择的是IDEA 2019.2，构建Maven工程等一堆通用操作，不清楚的自行百度。

我这边选择**elasticsearch-rest-high-level-client**方式来集成，发现这有个坑，开始没注意，踩了好久，就是要排除掉**elasticsearch、elasticsearch-rest-client**，这里没有选择spring-boot-starter-data-elasticsearch，因为最新版的starter现在依然是6.x版本号，并没有集成elasticsearch7.4.0，导致使用过程中有很多版本冲突，读者在选择的时候多加留意。

## 2.1. POM配置

~~~
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.4.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-client</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-client</artifactId>
    <version>7.4.0</version>
</dependency>
<dependency>
    <groupId>org.elasticsearch</groupId>
    <artifactId>elasticsearch</artifactId>
    <version>7.4.0</version>
</dependency>
~~~

## 2.2. yml配置

~~~
server:
  port: 9090
spring:
  datasource:
    name: mysql
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/springboot?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
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
es:
  host: 192.168.147.132
  port: 9200
  scheme: http

mybatis:
  mapperLocations: classpath:mapper/**/*.xml
~~~

这里定义

~~~
es:
  host: 192.168.147.132
  port: 9200
  scheme: http
~~~

## 2.3. 核心操作类

~~~
package xyz.wongs.weathertop.base.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class BaseElasticDao {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:30
     * @param idxName   索引名称
     * @param idxSQL    索引描述
     * @return void
     * @throws
     * @since
     */
    public void createIndex(String idxName,String idxSQL){

        try {

            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在,idxSql={}",idxName,idxSQL);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSQL, XContentType.JSON);
            //request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /** 断某个index是否存在
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:27
     * @param idxName index名
     * @return boolean
     * @throws
     * @since
     */
    public boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);

        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /** 设置分片
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 19:27
     * @param request
     * @return void
     * @throws
     * @since
     */
    public void buildSetting(CreateIndexRequest request){

        request.settings(Settings.builder().put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));
    }
    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:27
     * @param idxName index
     * @param entity    对象
     * @return void
     * @throws
     * @since
     */
    public void insertOrUpdateOne(String idxName, ElasticEntity entity) {

        IndexRequest request = new IndexRequest(idxName);
        request.id(entity.getId());
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /** 批量插入数据
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:26
     * @param idxName index
     * @param list 带插入列表
     * @return void
     * @throws
     * @since
     */
    public void insertBatch(String idxName, List<ElasticEntity> list) {

        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量删除
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:14
     * @param idxName index
     * @param idList    待删除列表
     * @return void
     * @throws
     * @since
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList) {

        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:14
     * @param idxName index
     * @param builder   查询参数
     * @param c 结果类对象
     * @return java.util.List<T>
     * @throws
     * @since
     */
    public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> c) {

        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 删除index
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:13
     * @param idxName
     * @return void
     * @throws
     * @since
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在",idxName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/17 17:13
     * @param idxName
     * @param builder
     * @return void
     * @throws
     * @since
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

~~~

# 3. 实战

## 3.1. 索引管理

由于在**BaseElasticDao**类中**createIndex**方法，我在Controller层将索引名称和索引SQL封装过，详细见[Github演示源码]([https://github.com/king-angmar/weathertop/tree/master/doc/image/elastich) 中**xyz.wongs.weathertop.palant.vo.IdxVo**

### 3.1.1. 创建索引

我们在创建索引过程中需要先判断是否有这个索引，否则不允许创建，由于我案例采用的是手动指定indexName和Settings，大家看的过程中要特别注意下，而且还有一点indexName必须是小写，如果是大写在创建过程中会有错误

![官方索引创建说明](https://i.loli.net/2019/11/19/5QCdq691l7EOwnf.png)

![索引名大写](https://i.loli.net/2019/12/05/W7YevidXZyQxSVK.png)

。详细的代码实现见如下：

~~~

/**
    * @Description 创建Elastic索引
    * @param idxVo
    * @return xyz.wongs.weathertop.base.message.response.ResponseResult
    * @throws
    * @date 2019/11/19 11:07
    */
@RequestMapping(value = "/createIndex",method = RequestMethod.POST)
public ResponseResult createIndex(@RequestBody IdxVo idxVo){
    ResponseResult response = new ResponseResult();
    try {
        //索引不存在，再创建，否则不允许创建
        if(!baseElasticDao.indexExist(idxVo.getIdxName())){
            String idxSql = JSONObject.toJSONString(idxVo.getIdxSql());
            log.warn(" idxName={}, idxSql={}",idxVo.getIdxName(),idxSql);
            baseElasticDao.createIndex(idxVo.getIdxName(),idxSql);
        } else{
            response.setStatus(false);
            response.setCode(ResponseCode.DUPLICATEKEY_ERROR_CODE.getCode());
            response.setMsg("索引已经存在，不允许创建");
        }
    } catch (Exception e) {
        response.setStatus(false);
        response.setCode(ResponseCode.ERROR.getCode());
        response.setMsg(ResponseCode.ERROR.getMsg());
    }
    return response;
}

~~~

创建索引需要设置分片，这里采用**Settings.Builder**方式，当然也可以JSON自定义方式，本文篇幅有限，不做演示。查看**xyz.wongs.weathertop.base.service.BaseElasticService.buildSetting**方法，这里是默认值。

> index.number_of_shards：分片数

> number_of_replicas：副本数

~~~

/** 设置分片
    * @author WCNGS@QQ.COM
    * @See
    * @date 2019/10/17 19:27
    * @param request
    * @return void
    * @throws
    * @since
    */
public void buildSetting(CreateIndexRequest request){
    request.settings(Settings.builder().put("index.number_of_shards",3)
            .put("index.number_of_replicas",2));

~~~

这时候我们通过Postman工具调用**Controller**，发现创建索引成功。

![新增索引](https://i.loli.net/2019/11/19/VwKLQceuNHA6vTJ.png)

再命令行执行**curl -H "Content-Type: application/json" -X GET "http://localhost:9200/_cat/indices?v"**，效果如图：

~~~

[elastic@localhost elastic]$ curl -H "Content-Type: application/json" -X GET "http://localhost:9200/_cat/indices?v"
health status index        uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   twitter      scSSD1SfRCio4F77Hh8aqQ   3   2          2            0      8.3kb          8.3kb
yellow open   idx_location _BJ_pOv0SkS4tv-EC3xDig   3   2          1            0        4kb            4kb
yellow open   wongs        uT13XiyjSW-VOS3GCqao8w   3   2          1            0      3.4kb          3.4kb
yellow open   idx_locat    Kr3wGU7JT_OUrRJkyFSGDw   3   2          3            0     13.2kb         13.2kb
yellow open   idx_copy_to  HouC9s6LSjiwrJtDicgY3Q   3   2          1            0        4kb            4kb

~~~

说明创建成功，这里总是通过命令行来验证，有点繁琐，既然我都有WEB服务，为什么不直接通过HTTP验证了？

### 3.1.2. 查看索引

我们写一个对外以HTTP+GET方式对外提供查询的服务。存在为TRUE，否则False.

~~~
/**
    * @Description 判断索引是否存在；存在-TRUE，否则-FALSE
    * @param index
    * @return xyz.wongs.weathertop.base.message.response.ResponseResult
    * @throws
    * @date 2019/11/19 18:48
    */
@RequestMapping(value = "/exist/{index}")
public ResponseResult indexExist(@PathVariable(value = "index") String index){

    ResponseResult response = new ResponseResult();
    try {
        if(!baseElasticDao.isExistsIndex(index)){
            log.error("index={},不存在",index);
            response.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
            response.setMsg(ResponseCode.RESOURCE_NOT_EXIST.getMsg());
        } else {
            response.setMsg(" 索引已经存在, " + index);
        }
    } catch (Exception e) {
        response.setCode(ResponseCode.NETWORK_ERROR.getCode());
        response.setMsg(" 调用ElasticSearch 失败！");
        response.setStatus(false);
    }
    return response;
}
~~~

### 3.1.3. 删除索引

删除的逻辑就比较简单，这里就不多说。

~~~
/** 删除index
    * @author WCNGS@QQ.COM
    * @See
    * @date 2019/10/17 17:13
    * @param idxName
    * @return void
    * @throws
    * @since
    */
public void deleteIndex(String idxName) {
    try {
        if (!this.indexExist(idxName)) {
            log.error(" idxName={} 已经存在",idxName);
            return;
        }
        restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
~~~

# 4. 源码

[Github演示源码](https://github.com/king-angmar/weathertop/tree/master/akkad-springboot/springboot-elasticsearch) ，记得给Star