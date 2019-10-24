package xyz.wongs.weathertop.handball.location.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.exception.WeathertopRuntimeException;
import xyz.wongs.weathertop.handball.location.entity.Location;
import xyz.wongs.weathertop.handball.location.service.LocationService;
import xyz.wongs.weathertop.base.message.response.ResponseResult;

import java.util.Optional;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {

	@Autowired
	@Qualifier("locationService")
	private LocationService locationService;

    @GetMapping("/findByLocationId")
    public ResponseResult findByLocationId(@RequestParam("id") Long id){
        Location location = locationService.selectByPrimaryKey(id);

        if(null==location){
            throw new WeathertopRuntimeException(ResponseCode.RESOURCE_NOT_EXIST);
        }
        return new ResponseResult(location);
    }

    @GetMapping("/getUrl")
    public String getUrl(@RequestParam("id") Long id, Model model){
        Location location = locationService.selectByPrimaryKey(id);

        Optional.ofNullable(location).orElseGet(()->{
            throw new WeathertopRuntimeException(ResponseCode.RESOURCE_NOT_EXIST);
        });

        model.addAttribute("fun",location.getUrl());
        return "add";
    }

}
