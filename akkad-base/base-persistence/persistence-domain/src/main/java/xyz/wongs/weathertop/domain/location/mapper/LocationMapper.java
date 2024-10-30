package xyz.wongs.weathertop.domain.location.mapper;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.domain.location.entity.Location;

import java.util.List;

public interface LocationMapper extends BaseMapper<Location,Long> {

    int deleteByPrimaryKey(Long id);

    Long insert(Location record);

    Long insertSelective(Location record);

    Location selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Location record);

    int updateByPrimaryKey(Location record);

    List<Location> getList(Location location);

    List<Location> getList2(Location location);

    int insertBatchByOn(List<Location> locations);

    int getCount(Location location);
}