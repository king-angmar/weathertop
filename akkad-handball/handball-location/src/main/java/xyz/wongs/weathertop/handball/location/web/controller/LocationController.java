package xyz.wongs.weathertop.handball.location.web.controller;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.wongs.weathertop.base.message.enums.ErrorCodeAndMsg;
import xyz.wongs.weathertop.base.message.exception.WeathertopException;
import xyz.wongs.weathertop.handball.location.entity.Location;
import xyz.wongs.weathertop.handball.location.service.LocationService;
import xyz.wongs.weathertop.base.message.response.Response;

/**
 *
 * <p>Title:LocationController </p>
 * <p>@Description: </p>
 * <p>Company: </p>
 * ----------------------------------------
 *	\\	 /\/\   /\|	|/\   /\/\	 //
 *	 \\^^  ^^^  ^^|_|^^  ^^^  ^^//
 *	  \\^   ^^^  ^/Ϡ\^   ^^^  ^//
 *	   \\^ ^    ^/___\^    ^ ^//
 *	    \\^ ^^ ^//   \\^ ^^ ^//
 *	     \\	^^/(/     \)\^^ //
 *	      \\^'//       \\'^//
 *	       .==.   खान          .==.
 * ----------------------------------------
 * @author: <a href="wcngs@qq.com">WCNGS</a>
 * @date:   2017年8月5日 下午11:08:19  *
 * @since JDK 1.7
 */
@Api(description="",value="location")
@RestController
@RequestMapping(value = "/locations")
public class LocationController {

	@Autowired
	@Qualifier("locationService")
	private LocationService locationService;

    @GetMapping("/findByLocationId")
    public Response findByLocationId(@RequestParam("id") Long id){
        Location location = locationService.selectByPrimaryKey(id);

        if(null==location){
            throw new WeathertopException(ErrorCodeAndMsg.Data_number_does_not_exist);
        }
        return new Response(location);
    }

}
