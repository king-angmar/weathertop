package xyz.wongs.weathertop.service.quanmin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.entity.quanmin.InformSms;
import xyz.wongs.weathertop.mapper.location.LocationMapper;
import xyz.wongs.weathertop.mapper.quanmin.InformSmsMapper;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class InformSmsService extends BaseService<InformSms, Long> {


    @Autowired
    private InformSmsMapper informSmsMapper;

    @Override
    protected BaseMapper<InformSms, Long> getMapper() {
        return informSmsMapper;
    }





}
