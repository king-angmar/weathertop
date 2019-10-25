package xyz.wongs.weathertop.shiro.sys.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.shiro.sys.entity.OptRole;
import xyz.wongs.weathertop.shiro.sys.entity.OptRoleExample;

import java.util.List;
import java.util.Map;

public interface OptRoleMapper extends BaseMapper<OptRole, Long> {
    int deleteByPrimaryKey(Long roleId);

    int insert(OptRole record);

    int insertSelective(OptRole record);

    List<OptRole> selectByExample(OptRoleExample example);

    OptRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(OptRole record);

    int updateByPrimaryKey(OptRole record);

    List<OptRole> selectRoleByAcctId(Map<String,?> map);

}