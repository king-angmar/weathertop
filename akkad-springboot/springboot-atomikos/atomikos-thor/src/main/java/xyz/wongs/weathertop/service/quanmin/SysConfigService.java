package xyz.wongs.weathertop.service.quanmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.entity.quanmin.SysConfig;
import xyz.wongs.weathertop.mapper.quanmin.SysConfigMapper;
import xyz.wongs.weathertop.service.location.LocationService;
import xyz.wongs.weathertop.dao.IdClazzUtils;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SysConfigService extends BaseService<SysConfig, Long> {

    @Autowired
    private IdClazzUtils idClazzUtils;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    protected BaseMapper<SysConfig, Long> getMapper() {
        return sysConfigMapper;
    }

//    @Autowired
//    private LocationService locationService;
    @Autowired
    private LocationService locationService;


    @Transactional(readOnly = false)
    public void testJTA() {

        Location location = new Location();
        location.setFlag("J");
        location.setLocalCode("2324");
        location.setLocalName("测试");
        location.setLv(9);
        location.setSupLocalCode("213");
        location.setUrl("www.baidu.com");

        locationService.insert(location);

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
