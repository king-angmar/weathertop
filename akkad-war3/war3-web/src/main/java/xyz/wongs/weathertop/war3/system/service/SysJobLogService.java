package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysJobLog;
import xyz.wongs.weathertop.war3.system.mapper.SysJobLogMapper;


@Service
public class SysJobLogService extends BaseService<SysJobLog, Long> {

    @Autowired
    private SysJobLogMapper sysJobLogMapper;

    @Override
    protected BaseMapper<SysJobLog, Long> getMapper() {
        return sysJobLogMapper;
    }
}