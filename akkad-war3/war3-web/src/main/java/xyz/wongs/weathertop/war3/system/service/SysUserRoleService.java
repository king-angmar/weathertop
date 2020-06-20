package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;
import xyz.wongs.weathertop.war3.system.mapper.SysUserRoleMapper;


@Service
public class SysUserRoleService extends BaseService<SysUserRole, Long> {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    protected BaseMapper<SysUserRole, Long> getMapper() {
        return sysUserRoleMapper;
    }
}