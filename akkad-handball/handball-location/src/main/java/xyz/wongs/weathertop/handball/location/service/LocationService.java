package xyz.wongs.weathertop.handball.location.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.wongs.weathertop.handball.location.entity.Location;
import xyz.wongs.weathertop.handball.location.mapper.LocationMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;

import java.util.List;

@Slf4j
@Transactional
@Service
public class LocationService extends BaseService<Location, Long> {


    @Autowired
    private LocationMapper locationMapper;

    @Override
    protected BaseMapper<Location, Long> getMapper() {
        return locationMapper;
    }

    public int insertBatchByOn(List<Location> locations){
        return locationMapper.insertBatchByOn(locations);
    }


    public PageInfo<Location> getLocationsByLv(int lv,int pageNum,int pageSize){
        PageInfo<Location> pageInfo =null;
        try {
            PageHelper.startPage(pageNum,pageSize);
            Location location = new Location();
            location.setLv(lv);
            List<Location> locations = locationMapper.getList2(location);
            pageInfo = new PageInfo<>(locations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageInfo;
    }


}
