package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysOperLog;
import xyz.wongs.weathertop.war3.system.mapper.SysOperLogMapper;


@Service
public class SysOperLogService extends BaseService<SysOperLog, Long> {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    protected BaseMapper<SysOperLog, Long> getMapper() {
        return sysOperLogMapper;
    }
}