package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysDictType;
import xyz.wongs.weathertop.war3.system.mapper.SysDictTypeMapper;


@Service
public class SysDictTypeService extends BaseService<SysDictType, Long> {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Override
    protected BaseMapper<SysDictType, Long> getMapper() {
        return sysDictTypeMapper;
    }
}