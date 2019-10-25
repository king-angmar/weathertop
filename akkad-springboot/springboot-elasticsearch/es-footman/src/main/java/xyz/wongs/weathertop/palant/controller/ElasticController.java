package xyz.wongs.weathertop.palant.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.dao.BaseElasticDao;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.palant.vo.IdxVo;

@Slf4j
@RequestMapping("/elastic")
@RestController
public class ElasticController {


    @Autowired
    BaseElasticDao baseElasticDao;

    @RequestMapping(value = "/}")
    public ResponseResult index(String index){
        ResponseResult response = new ResponseResult();
        return response;
    }

    @RequestMapping(value = "/createIndex",method = RequestMethod.POST)
    public ResponseResult createIndex(@RequestBody IdxVo idxVo){
        ResponseResult response = new ResponseResult();
        String idxSql = JSONObject.toJSONString(idxVo.getIdxSql());
        log.warn(" idxName={}, idxSql={}",idxVo.getIdxName(),idxSql);
        baseElasticDao.createIndex(idxVo.getIdxName(),idxSql);
        return response;
    }


    @RequestMapping(value = "/exist/{index}")
    public ResponseResult indexExist(@PathVariable(value = "index") String index){
        ResponseResult response = new ResponseResult();
        try {
            if(baseElasticDao.indexExist(index)){
                log.error("index={},不存在",index);
                response.setCode(ResponseCode.RESOURCE_NOT_EXIST.getCode());
                response.setMsg(ResponseCode.RESOURCE_NOT_EXIST.getMsg());
            }
        } catch (Exception e) {
            response.setCode(ResponseCode.NETWORK_ERROR.getCode());
            response.setMsg(" 调用ElasticSearch 失败！");
            response.setStatus(false);
        }
        return response;
    }

    @RequestMapping(value = "/del/{index}")
    public ResponseResult indexDel(@PathVariable(value = "index") String index){
        ResponseResult response = new ResponseResult();
        try {
            baseElasticDao.deleteIndex(index);
        } catch (Exception e) {
            response.setCode(ResponseCode.NETWORK_ERROR.getCode());
            response.setMsg(" 调用ElasticSearch 失败！");
            response.setStatus(false);
        }
        return response;
    }
}
