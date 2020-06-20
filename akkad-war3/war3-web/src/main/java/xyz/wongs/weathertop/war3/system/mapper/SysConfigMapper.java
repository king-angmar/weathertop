package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysConfig;

@MyBatisMapper
public interface SysConfigMapper extends BaseMapper<SysConfig,Long> {

    int deleteByPrimaryKey(Integer configId);

    Long insert(SysConfig record);

    Long insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer configId);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    SysConfig selectConfig(SysConfig record);
}