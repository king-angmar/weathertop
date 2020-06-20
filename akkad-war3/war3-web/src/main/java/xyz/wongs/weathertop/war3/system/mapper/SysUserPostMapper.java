package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysUserPost;

@MyBatisMapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost,Long> {
    int deleteByPrimaryKey(Long userPostId);

    Long insert(SysUserPost record);

    Long insertSelective(SysUserPost record);

    SysUserPost selectByPrimaryKey(Long userPostId);

    int updateByPrimaryKeySelective(SysUserPost record);

    int updateByPrimaryKey(SysUserPost record);
}