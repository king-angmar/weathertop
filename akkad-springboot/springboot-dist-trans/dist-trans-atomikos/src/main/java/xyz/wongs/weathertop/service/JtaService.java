package xyz.wongs.weathertop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.entity.quanmin.InformSms;
import xyz.wongs.weathertop.service.location.LocationService;
import xyz.wongs.weathertop.service.quanmin.InformSmsService;

import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
public class JtaService {

    @Autowired
    private InformSmsService informSmsService;

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

        InformSms informSms = new InformSms();
        informSms.setContent("joda");
        informSms.setCreateDate(new Date());
        informSms.setServiceType("wy");
        informSms.setStateDate(new Date());
        informSms.setStateRemark("测试分布式事务");
        informSmsService.insert(informSms);

//		int i = 10/0;
    }

}
