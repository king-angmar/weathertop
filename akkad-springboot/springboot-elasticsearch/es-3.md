
# 1. 数据管理

## 1.1. 新增数据

### 1.1.1. 单实例新增

http方式提交数据，案例中我将数据格式做了规范，提交过程中需要指定索引名，以及JSON格式数据，这样尽可能在实际过程中做到通用。

为此我用交互对象Vo来接受前端传递来的数据，再解析该Vo，获取要提交的目标索引。

- 前端Web端实现

~~~

/**
    * @Description 新增数据
    * @param elasticDataVo
    * @return xyz.wongs.weathertop.base.message.response.ResponseResult
    * @throws
    * @date 2019/11/20 17:10
    */
@RequestMapping(value = "/add",method = RequestMethod.POST)
public ResponseResult add(@RequestBody ElasticDataVo elasticDataVo){
    ResponseResult response = getResponseResult();
    try {
        if(!StringUtils.isNotEmpty(elasticDataVo.getIdxName())){
            response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
            response.setMsg("索引为空，不允许提交");
            response.setStatus(false);
            log.warn("索引为空");
            return response;
        }
        ElasticEntity elasticEntity = new ElasticEntity();
        elasticEntity.setId(elasticDataVo.getElasticEntity().getId());
        elasticEntity.setData(elasticDataVo.getElasticEntity().getData());

        baseElasticService.insertOrUpdateOne(elasticDataVo.getIdxName(), elasticEntity);

    } catch (Exception e) {
        response.setCode(ResponseCode.ERROR.getCode());
        response.setMsg("服务忙，请稍后再试");
        response.setStatus(false);
        log.error("插入数据异常，metadataVo={},异常信息={}", elasticDataVo.toString(),e.getMessage());
    }
    return response;
}
~~~

- ElasticDataVo类

~~~

package xyz.wongs.weathertop.palant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;

/**
 * @ClassName ElasticDataVo
 * @Description http交互Vo对象
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/11/21 9:10
 * @Version 1.0.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticDataVo<T> {

    /**
     * 索引名
     */
    private String idxName;
    /**
     * 数据存储对象
     */
    private ElasticEntity elasticEntity;

}


~~~

- ElasticEntity类

~~~

package xyz.wongs.weathertop.base.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Map;

/**
 * @ClassName ElasticEntity
 * @Description  数据存储对象
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/11/21 9:10
 * @Version 1.0.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity<T> {

    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Map data;
}

~~~

- Postman提交

![测试用例](https://i.loli.net/2019/11/21/m6jiQpTP5DW2ouf.png)

### 1.1.2. 批量提交


## 1.2. 条件查询

为了通用，我在web端也分装了一层Vo，为了演示需要我仅写一个 match 精确匹配的查询。

- 前端web实现

~~~
/**
    * @Description
    * @param queryVo 查询实体对象
    * @return xyz.wongs.weathertop.base.message.response.ResponseResult
    * @throws
    * @date 2019/11/21 9:31
    */
@RequestMapping(value = "/get",method = RequestMethod.GET)
public ResponseResult get(@RequestBody QueryVo queryVo){

    ResponseResult response = getResponseResult();

    if(!StringUtils.isNotEmpty(queryVo.getIdxName())){
        response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
        response.setMsg("索引为空，不允许提交");
        response.setStatus(false);
        log.warn("索引为空");
        return response;
    }

    try {
        Class<?> clazz = ElasticUtil.getClazz(queryVo.getClassName());
        Map<String,Object> params = queryVo.getQuery().get("match");
        Set<String> keys = params.keySet();
        MatchQueryBuilder queryBuilders=null;
        for(String ke:keys){
            queryBuilders = QueryBuilders.matchQuery(ke, params.get(ke));
        }
        if(null!=queryBuilders){
            SearchSourceBuilder searchSourceBuilder = ElasticUtil.initSearchSourceBuilder(queryBuilders);
            List<?> data = baseElasticService.search(queryVo.getIdxName(),searchSourceBuilder,clazz);
            response.setData(data);
        }
    } catch (Exception e) {
        response.setCode(ResponseCode.ERROR.getCode());
        response.setMsg("服务忙，请稍后再试");
        response.setStatus(false);
        log.error("查询数据异常，metadataVo={},异常信息={}", queryVo.toString(),e.getMessage());
    }
    return response;
}

~~~

- QueryVo

~~~

/** 查询Vo对象
 * @ClassName QueryVo
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/10/25 23:16
 * @Version 1.0.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryVo {

    /**
     * 索引名
     */
    private String idxName;
    /**
     * 需要反射的实体类型，用于对查询结果的封装
     */
    private String className;
    /**
     * 具体条件
     */
    private Map<String,Map<String,Object>> query;
}

~~~

- Postman提交

![条件查询](https://i.loli.net/2019/11/21/pceid3Fxk8ynKD7.png)

## 1.3. 删除数据

### 1.3.1. 单实例删除

~~~
/**
    * @Description 删除
    * @param elasticDataVo
    * @return xyz.wongs.weathertop.base.message.response.ResponseResult
    * @throws
    * @date 2019/11/21 9:56
    */
@RequestMapping(value = "/delete",method = RequestMethod.POST)
public ResponseResult delete(@RequestBody ElasticDataVo elasticDataVo){
    ResponseResult response = getResponseResult();
    try {
        if(!StringUtils.isNotEmpty(elasticDataVo.getIdxName())){
            response.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
            response.setMsg("索引为空，不允许提交");
            response.setStatus(false);
            log.warn("索引为空");
            return response;
        }
        baseElasticService.deleteOne(elasticDataVo.getIdxName(),elasticDataVo.getElasticEntity());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return response;

}
~~~

![删除数据](https://i.loli.net/2019/11/21/bm472MhqDjJPRFl.png)


### 1.3.2. 批量删除


# 2. 源码

[Github演示源码](https://github.com/king-angmar/weathertop/tree/master/akkad-springboot/springboot-elasticsearch) ，记得给Star