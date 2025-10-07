package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysRoleMenu;

@MyBatisMapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu,Long> {
    int deleteByPrimaryKey(Long roleMenuId);

    Long insert(SysRoleMenu record);

    Long insertSelective(SysRoleMenu record);

    SysRoleMenu selectByPrimaryKey(Long roleMenuId);

    int updateByPrimaryKeySelective(SysRoleMenu record);

    int updateByPrimaryKey(SysRoleMenu record);

    int selectCountRoleMenuByMenuId(Long menuId);
}