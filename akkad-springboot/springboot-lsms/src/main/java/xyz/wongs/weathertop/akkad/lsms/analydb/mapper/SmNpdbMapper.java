package xyz.wongs.weathertop.akkad.lsms.analydb.mapper;

import xyz.wongs.weathertop.akkad.lsms.analydb.entity.SmNpdb;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import java.util.List;

public interface SmNpdbMapper extends BaseMapper<SmNpdb, Long> {

    int deleteByPrimaryKey(Long npcode);

    int insert(SmNpdb record);

    int insertSelective(SmNpdb record);

    SmNpdb selectByPrimaryKey(Long npcode);

    int updateByPrimaryKeySelective(SmNpdb record);

    int updateByPrimaryKey(SmNpdb record);

    int insertBatchByOn(List<SmNpdb> list);
}