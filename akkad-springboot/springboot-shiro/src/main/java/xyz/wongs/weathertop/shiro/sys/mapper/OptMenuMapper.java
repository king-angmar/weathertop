package xyz.wongs.weathertop.shiro.sys.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.shiro.sys.entity.OptMenu;
import xyz.wongs.weathertop.shiro.sys.entity.OptMenuExample;

import java.util.List;
import java.util.Map;

public interface OptMenuMapper extends BaseMapper<OptMenu, Long> {
    int deleteByPrimaryKey(Long menuId);

    int insert(OptMenu record);

    int insertSelective(OptMenu record);

    List<OptMenu> selectByExample(OptMenuExample example);

    OptMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(OptMenu record);

    int updateByPrimaryKey(OptMenu record);

    List<OptMenu> selectMenuByRoleId(Map map);

}