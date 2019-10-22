package xyz.wongs.weathertop.shiro.sys.mapper;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.shiro.sys.entity.OptAcctRolRef;

public interface OptAcctRolRefMapper extends BaseMapper<OptAcctRolRef, Long> {
    int deleteByPrimaryKey(Long acctRolRefId);

    int insert(OptAcctRolRef record);

    int insertSelective(OptAcctRolRef record);

    OptAcctRolRef selectByPrimaryKey(Long acctRolRefId);

    int updateByPrimaryKeySelective(OptAcctRolRef record);

    int updateByPrimaryKey(OptAcctRolRef record);
}