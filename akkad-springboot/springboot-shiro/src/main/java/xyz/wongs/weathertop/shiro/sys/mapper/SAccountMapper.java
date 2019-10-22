package xyz.wongs.weathertop.shiro.sys.mapper;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.shiro.sys.entity.SAccount;

public interface SAccountMapper extends BaseMapper<SAccount, Integer> {

    int deleteByPrimaryKey(Integer accountid);

    int insert(SAccount record);

    int insertSelective(SAccount record);

    SAccount selectByPrimaryKey(Integer accountid);

    int updateByPrimaryKeySelective(SAccount record);

    int updateByPrimaryKey(SAccount record);
}