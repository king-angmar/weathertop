package xyz.wongs.weathertop.palant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.dao.BaseElasticDao;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.Response;

@Slf4j
@RequestMapping("/elastic")
@RestController
public class ElasticController {


    @Autowired
    BaseElasticDao baseElasticDao;

    @RequestMapping(value = "/}")
    public Response index(String index){
        Response response = new Response();
        return response;
    }

    @RequestMapping(value = "/exist/{index}")
    public Response indexExist(@PathVariable(value = "index") String index){
        Response response = new Response();
        try {
            baseElasticDao.indexExist(index);
        } catch (Exception e) {
            response.setCode(ResponseCode.NETWORK_ERROR.getCode());
            response.setMsg(" 调用ElasticSearch 失败！");
            response.setStatus(false);
        }
        return response;
    }

    @RequestMapping(value = "/del/{index}")
    public Response indexDel(@PathVariable(value = "index") String index){
        Response response = new Response();
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
