package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;

@MyBatisMapper
public interface SysUserOnlineMapper extends BaseMapper<SysUserOnline,Long> {

    int deleteByPrimaryKey(Long userOnlineId);

    Long insert(SysUserOnline record);

    Long insertSelective(SysUserOnline record);

    SysUserOnline selectByPrimaryKey(Long userOnlineId);

    int updateByPrimaryKeySelective(SysUserOnline record);

    int updateByPrimaryKey(SysUserOnline record);
}