package xyz.wongs.weathertop.war3.system.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysJob;

@MyBatisMapper
public interface SysJobMapper extends BaseMapper<SysJob,Long> {
    int deleteByPrimaryKey(@Param("jobId") Long jobId, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    Long insert(SysJob record);

    Long insertSelective(SysJob record);

    SysJob selectByPrimaryKey(@Param("jobId") Long jobId, @Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    int updateByPrimaryKeySelective(SysJob record);

    int updateByPrimaryKey(SysJob record);
}