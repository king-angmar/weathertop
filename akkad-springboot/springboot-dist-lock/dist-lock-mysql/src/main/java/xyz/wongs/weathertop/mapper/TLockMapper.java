package xyz.wongs.weathertop.mapper;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.entity.TLock;

public interface TLockMapper extends BaseMapper<TLock,Integer> {

    int deleteByPrimaryKey(Integer id);

    int insert(TLock record);

    int insertSelective(TLock record);

    TLock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TLock record);

    int updateByPrimaryKey(TLock record);
}