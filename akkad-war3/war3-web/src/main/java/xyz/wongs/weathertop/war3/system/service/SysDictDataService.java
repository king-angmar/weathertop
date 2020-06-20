package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysDictData;
import xyz.wongs.weathertop.war3.system.mapper.SysDictDataMapper;


@Service
public class SysDictDataService extends BaseService<SysDictData, Long> {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    protected BaseMapper<SysDictData, Long> getMapper() {
        return sysDictDataMapper;
    }
}