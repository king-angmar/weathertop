package xyz.wongs.weathertop.handball.location.mapper;


import xyz.wongs.weathertop.handball.location.entity.Location;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import java.util.List;

public interface LocationMapper extends BaseMapper<Location,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Location record);

    int insertSelective(Location record);

    Location selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Location record);

    int updateByPrimaryKey(Location record);

    int insertBatchByOn(List<Location> locations);


    List<Location> getList(Location location);

    List<Location> getList2(Location location);
}