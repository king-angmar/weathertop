package xyz.wongs.weathertop.sys.web;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.domain.location.entity.Location;
import xyz.wongs.weathertop.domain.location.service.LocationService;

import java.util.List;

/**
 * <p>Title:LocationController </p>
 * <p>@Description: </p>
 * <p>Company: </p>
 * ----------------------------------------
 * \\	 /\/\   /\|	|/\   /\/\	 //
 * \\^^  ^^^  ^^|_|^^  ^^^  ^^//
 * \\^   ^^^  ^/Ϡ\^   ^^^  ^//
 * \\^ ^    ^/___\^    ^ ^//
 * \\^ ^^ ^//   \\^ ^^ ^//
 * \\	^^/(/     \)\^^ //
 * \\^'//       \\'^//
 * .==.   खान          .==.
 * ----------------------------------------
 *
 * @author: <a href="wcngs@qq.com">WCNGS</a>
 * @date: 2017年8月5日 下午11:08:19  *
 * @since JDK 1.7
 */
@Api(value = "location")
@RestController
@RequestMapping(value = "/locations")
public class LocationController {


    @Autowired
    private LocationService locationService;



    /**
     * @param lv
     * @return List<LocationEntity>
     * @Title: getLocationListByLevel
     * @Description: 请求参数在URL中，需要在 @ApiImplicitParam 中加上 "paramType="path""
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
    public List<Location> getLocationListByLevel(@PathVariable(value = "lv") Integer lv) {
        return locationService.getLocationListByLevel(lv);
    }

    @ApiOperation(value = "获取行政区域", notes = "根据主键获取行政区域")
    @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "id={id}")
    public List<Location> getLocationListByLevel(@PathVariable Long id) {
        return locationService.getLocationListById(id);
    }

    @ApiOperation(value = "getLocationByID", notes = "根据ID获取区域")
    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public List<Location> getLocationByID(@PathVariable(value = "id") Integer id) {
        Long lid = Long.valueOf(id);
        return locationService.getLocationListById(lid);
    }

    @ApiOperation(value = "getLocationByLevel", notes = "根据层级获取区域")
    @GetMapping(value = "/level/{lv}")
    @ResponseBody
    public List<Location> getLocationByLv(@PathVariable(value = "lv") Integer lv) {
        return locationService.getLocationListByLevel(lv);

    }
}
