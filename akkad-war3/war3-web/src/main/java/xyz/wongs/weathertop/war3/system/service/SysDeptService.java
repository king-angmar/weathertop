package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysDept;
import xyz.wongs.weathertop.war3.system.mapper.SysDeptMapper;


@Service
public class SysDeptService extends BaseService<SysDept, Long> {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    protected BaseMapper<SysDept, Long> getMapper() {
        return sysDeptMapper;
    }
}