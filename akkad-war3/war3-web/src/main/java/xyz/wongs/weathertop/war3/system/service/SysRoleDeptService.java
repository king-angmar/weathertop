package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysRoleDept;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleDeptMapper;


@Service
public class SysRoleDeptService extends BaseService<SysRoleDept, Long> {

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Override
    protected BaseMapper<SysRoleDept, Long> getMapper() {
        return sysRoleDeptMapper;
    }
}