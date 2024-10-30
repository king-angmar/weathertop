package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysJobLog;

@MyBatisMapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog,Long> {
    int deleteByPrimaryKey(Long jobLogId);

    Long insert(SysJobLog record);

    Long insertSelective(SysJobLog record);

    SysJobLog selectByPrimaryKey(Long jobLogId);

    int updateByPrimaryKeySelective(SysJobLog record);

    int updateByPrimaryKey(SysJobLog record);
}