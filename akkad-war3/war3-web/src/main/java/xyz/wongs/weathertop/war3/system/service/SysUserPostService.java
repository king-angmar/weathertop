package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysUserPost;
import xyz.wongs.weathertop.war3.system.mapper.SysUserPostMapper;


@Service
public class SysUserPostService extends BaseService<SysUserPost, Long> {

    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    @Override
    protected BaseMapper<SysUserPost, Long> getMapper() {
        return sysUserPostMapper;
    }
}