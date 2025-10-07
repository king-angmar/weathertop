package xyz.wongs.weathertop.sys.web;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.base.web.BaseController;
import xyz.wongs.weathertop.domain.location.service.LocationService;

/**
 * @ClassName LocationController
 * @Description
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/12/15 16:58
 * @Version 1.0.0
 */
@Api(value = "location")
@RestController
@RequestMapping(value = "/locations")
public class LocationController extends BaseController {


    @Autowired
    private LocationService locationService;

    /**
     * @Description
     * @param lv
     * @return xyz.wongs.weathertop.base.message.response.ResponseResult
     * @throws
     * @date 2019/12/15 17:01
     */
    @ApiOperation(value = "获取行政区域列表", notes = "根据层级获取行政列表")
    @ApiImplicitParam(name = "lv", value = "层级", required = true, dataType = "Integer", paramType = "path")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 401, message = "未授权客户机访问数据"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping(value = "/{lv}")
    public ResponseResult getLocationListByLevel(@PathVariable(value = "lv") Integer lv) {
        ResponseResult result = getResponseResult();
        result.setData(locationService.getLocationListByLevel(lv));
        return result;
    }
}
