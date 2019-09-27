package xyz.wongs.weathertop.service.location;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.entity.location.Location;
import xyz.wongs.weathertop.mapper.location.LocationMapper;

import java.util.List;

@Slf4j
@Transactional
@Service
public class LocationService {


    @Autowired
    private LocationMapper locationMapper;

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

    @Transactional(readOnly = false)
    public void insert(Location location){
        locationMapper.insert(location);
    }


}
