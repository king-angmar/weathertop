package xyz.wongs.weathertop.deno.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.deno.entity.RedisLock;

public interface RedisLockMapper extends BaseMapper<RedisLock,Integer> {
    int deleteByPrimaryKey(Integer id);

    Long insert(RedisLock record);

    Long insertSelective(RedisLock record);

    RedisLock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedisLock record);

    int updateByPrimaryKey(RedisLock record);
}