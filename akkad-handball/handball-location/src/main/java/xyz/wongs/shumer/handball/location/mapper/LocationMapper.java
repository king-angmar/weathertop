package xyz.wongs.shumer.handball.location.mapper;


import xyz.wongs.shumer.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.shumer.handball.location.entity.Location;
import xyz.wongs.shumer.handball.location.entity.LocationExample;

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