package xyz.wongs.weathertop.sys.location.mapper;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.sys.location.entity.Location;

import java.util.List;
import java.util.Map;

public interface LocationMapper extends BaseMapper<Location,Long> {

    int deleteByPrimaryKey(Long id);

    int insert(Location record);

    int insertSelective(Location record);

    Location selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Location record);

    int updateByPrimaryKey(Location record);

    List<Location> getList(Location location);

}