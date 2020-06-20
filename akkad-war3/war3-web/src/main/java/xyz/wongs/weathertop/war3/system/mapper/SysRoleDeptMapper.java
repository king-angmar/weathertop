package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysRoleDept;

@MyBatisMapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept,Long> {
    int deleteByPrimaryKey(Long roleDeptId);

    Long insert(SysRoleDept record);

    Long insertSelective(SysRoleDept record);

    SysRoleDept selectByPrimaryKey(Long roleDeptId);

    int updateByPrimaryKeySelective(SysRoleDept record);

    int updateByPrimaryKey(SysRoleDept record);
}