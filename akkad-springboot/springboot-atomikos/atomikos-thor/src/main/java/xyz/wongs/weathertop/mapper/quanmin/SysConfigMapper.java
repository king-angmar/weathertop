package xyz.wongs.weathertop.mapper.quanmin;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.entity.quanmin.SysConfig;

@MyBatisMapper
public interface SysConfigMapper extends BaseMapper<SysConfig,Long> {

    int deleteByPrimaryKey(Long id);

    int insert(SysConfig sysConfig);

    int insertSelective(SysConfig sysConfig);

    SysConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfig sysConfig);

    int updateByPrimaryKey(SysConfig sysConfig);
}