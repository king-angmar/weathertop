package xyz.wongs.weathertop.palant.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageParams;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.persistence.mybatis.page.PaginationInfo;
import xyz.wongs.weathertop.base.service.BaseElasticService;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.location.entity.Location;
import xyz.wongs.weathertop.location.service.LocationService;
import xyz.wongs.weathertop.palant.utils.ElasticUtil;
import xyz.wongs.weathertop.palant.vo.ElasticDataVo;
import xyz.wongs.weathertop.palant.vo.QueryVo;

import java.util.*;

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


    /**
     * @Description
     * @param elasticDataVo
     * @return xyz.wongs.weathertop.base.message.response.ResponseResult
     * @throws
     * @date 2019/11/20 17:10
     */
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

    /**
     * @Description
     * @param index 初始化Location区域，写入数据。
     * @return xyz.wongs.weathertop.base.message.response.ResponseResult
     * @throws
     * @date 2019/11/20 17:10
     */
    @RequestMapping(value = "/addLocation/{index}")
    public ResponseResult addLocation(@PathVariable(value = "index") String index){
        ResponseResult response = getResponseResult();
        try {
            for(int lv=0;lv<4;lv++){
//                if(lv==2){
//                    return response;
//                }
                addLocationPage(1,100,index,lv);
            }

        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg("服务忙，请稍后再试");
            response.setStatus(false);
        }
        return response;
    }

    public void addLocationPage(int pageNum,int pageSize,String index,int lv){
        Location location = new Location();
        location.setLv(lv);
        PageHelper.startPage(pageNum, pageSize);
        List<Location> locations = locationService.getList2(location);
        PageInfo pageInfo = new PageInfo(locations);
        if(!pageInfo.getList().isEmpty()){
            log.error("第{}层级，第{}页，开始入ES库",lv,pageNum);
            insertDatas(index,locations);
            if(pageInfo.isHasNextPage()){
                addLocationPage(pageInfo.getNextPage(),pageSize,index,lv);
            }
        }
    }


    public void insertDatas(String idxName,List<Location> locations){
        Gson gson = new Gson();
        List<ElasticEntity> elasticEntitys = new ArrayList<ElasticEntity>(locations.size());
        for(Location _loca:locations){
            ElasticEntity elasticEntity = new ElasticEntity();
            elasticEntity.setId(_loca.getId().toString());
            elasticEntity.setData(gson.toJson(_loca));
            elasticEntitys.add(elasticEntity);
//            log.error(_loca.toString());
        }
        baseElasticService.insertBatch(idxName,elasticEntitys);
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
