package xyz.wongs.weathertop.mapper.quanmin;

import xyz.wongs.weathertop.entity.quanmin.SysConfig;

public interface SysConfigMapper{

    int deleteByPrimaryKey(Long id);

    int insert(SysConfig sysConfig);

    int insertSelective(SysConfig sysConfig);

    SysConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysConfig sysConfig);

    int updateByPrimaryKey(SysConfig sysConfig);
}