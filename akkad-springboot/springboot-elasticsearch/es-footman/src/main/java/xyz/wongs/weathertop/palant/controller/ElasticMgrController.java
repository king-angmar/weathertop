package xyz.wongs.weathertop.palant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.dao.BaseElasticDao;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.palant.vo.LocationVo;

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
    private BaseElasticDao baseElasticDao;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseResult add(@RequestBody LocationVo locationVo){
        ResponseResult response = new ResponseResult();
        try {
            ElasticEntity elasticEntity = new ElasticEntity();
            elasticEntity.setId(locationVo.getLocationEntity().getId());
            elasticEntity.setData(locationVo.getLocationEntity().getLocation());

            baseElasticDao.insertOrUpdateOne(locationVo.getIdxName(), elasticEntity);
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg("服务忙，请稍后再试");
            response.setStatus(false);
            log.error("插入数据异常，metadataVo={},异常信息={}", locationVo.toString(),e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ResponseResult get(@RequestBody LocationVo locationVo){
        ResponseResult response = new ResponseResult();
        try {
            ElasticEntity elasticEntity = new ElasticEntity();
            elasticEntity.setId(locationVo.getLocationEntity().getId());
            elasticEntity.setData(locationVo.getLocationEntity().getLocation());

            baseElasticDao.insertOrUpdateOne(locationVo.getIdxName(), elasticEntity);
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR.getCode());
            response.setMsg("服务忙，请稍后再试");
            response.setStatus(false);
            log.error("插入数据异常，metadataVo={},异常信息={}", locationVo.toString(),e.getMessage());
        }
        return response;
    }


}
