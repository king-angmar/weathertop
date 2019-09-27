package xyz.wongs.weathertop.mapper.location;


import xyz.wongs.weathertop.entity.location.Location;

import java.util.List;

public interface LocationMapper {
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