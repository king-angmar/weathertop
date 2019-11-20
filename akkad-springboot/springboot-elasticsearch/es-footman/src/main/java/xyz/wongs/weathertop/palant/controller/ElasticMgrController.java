package xyz.wongs.weathertop.palant.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.service.BaseElasticService;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.location.entity.Location;
import xyz.wongs.weathertop.location.service.LocationService;
import xyz.wongs.weathertop.palant.utils.ElasticUtil;
import xyz.wongs.weathertop.palant.vo.ElasticDataVo;
import xyz.wongs.weathertop.palant.vo.QueryVo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 数据管理
 * @ClassName ElasticMgrController
 * @Description
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/10/25 16:55
 * @Version 1.0.0
 */
@Slf4j
@RequestMapping("/elasticMgr")
@RestController
public class ElasticMgrController {

    @Autowired
    private BaseElasticService baseElasticService;

    @Autowired
    LocationService locationService;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseResult add(@RequestBody ElasticDataVo elasticDataVo){
        ResponseResult response = getResponseResult();
        try {
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

    @RequestMapping(value = "/addLocation/{index}")
    public ResponseResult addLocation(@PathVariable(value = "index") String index){
        ResponseResult response = getResponseResult();
        try {
            Location location = new Location();
            location.setSupLocalCode("0");
            List<Location> locations = locationService.getList2(location);
            if(locations.isEmpty()){
                response.setMsg("数据为空");
                log.error(" 源数据为空");
                return response;
            }
            Gson gson = new Gson();
            for(Location _loca:locations){
                ElasticEntity elasticEntity = new ElasticEntity();
                elasticEntity.setId(_loca.getId().toString());
                elasticEntity.setData(gson.toJson(_loca));
                baseElasticService.insertOrUpdateOne(index, elasticEntity);
            }
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg("服务忙，请稍后再试");
            response.setStatus(false);
        }
        return response;
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ResponseResult get(@RequestBody QueryVo queryVo){
        ResponseResult response = getResponseResult();
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

    public ResponseResult getResponseResult(){
        return new ResponseResult();
    }
}
