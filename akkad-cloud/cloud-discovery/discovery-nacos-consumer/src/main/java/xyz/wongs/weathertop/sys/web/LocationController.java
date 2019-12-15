package xyz.wongs.weathertop.sys.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.base.web.BaseController;

/**
 * @ClassName LocationController
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/12/15 16:58
 * @Version 1.0.0
*/
@RestController
@RequestMapping(value = "/locations")
public class LocationController extends BaseController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/consumer/{lv}")
    public ResponseResult echo(@PathVariable(value = "lv") Integer lv) {
        return restTemplate.getForObject("http://discovery-nacos-provider/locations/" + lv, ResponseResult.class);
    }
}
