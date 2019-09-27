package xyz.wongs.weathertop.service.quanmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.mapper.location.LocationMapper;
import xyz.wongs.weathertop.entity.quanmin.SysConfig;
import xyz.wongs.weathertop.mapper.quanmin.SysConfigMapper;
import xyz.wongs.weathertop.util.IdClazzUtils;

import java.util.Date;

@Slf4j
@Transactional
@Service
public class SysConfigService {

    @Autowired
    private IdClazzUtils idClazzUtils;

    @Autowired
    private SysConfigMapper sysConfigMapper;

//    @Autowired
//    private LocationService locationService;
    @Autowired
    private LocationMapper locationMapper;


    @Transactional(readOnly = false)
    public void testJTA() {

        Location location = new Location();
        location.setFlag("J");
        location.setLocalCode("2324");
        location.setLocalName("测试");
        location.setLv(9);
        location.setSupLocalCode("213");
        location.setUrl("www.baidu.com");

        locationMapper.insert(location);

        SysConfig sysConfig = new SysConfig();
        sysConfig.setId(0L);
        sysConfig.setKey("");
        sysConfig.setValue("");
        sysConfig.setStatus((byte)0);
        sysConfig.setRemark("");
        sysConfig.setGmtCreate(new Date());
        sysConfig.setGmtModified(new Date());
        sysConfigMapper.insert(sysConfig);


//		int i = 10/0;
    }

}
