package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysLoginInfo;
import xyz.wongs.weathertop.war3.system.mapper.SysLoginInfoMapper;


@Service
public class SysLoginInfoService extends BaseService<SysLoginInfo, Long> {

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    protected BaseMapper<SysLoginInfo, Long> getMapper() {
        return sysLoginInfoMapper;
    }
}