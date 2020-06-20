package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysJob;
import xyz.wongs.weathertop.war3.system.mapper.SysJobMapper;


@Service
public class SysJobService extends BaseService<SysJob, Long> {

    @Autowired
    private SysJobMapper sysJobMapper;

    @Override
    protected BaseMapper<SysJob, Long> getMapper() {
        return sysJobMapper;
    }
}