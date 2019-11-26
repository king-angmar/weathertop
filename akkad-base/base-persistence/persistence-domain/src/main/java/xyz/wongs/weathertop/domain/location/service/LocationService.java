package xyz.wongs.weathertop.domain.location.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.domain.location.entity.Location;
import xyz.wongs.weathertop.domain.location.mapper.LocationMapper;

import java.util.List;

@Service(value="locationService")
@Transactional(readOnly = true)
public class LocationService extends BaseService<Location, Long> {


	public List<Location> getLocationBySupLocalCode(String supLocalCode) {
		// TODO Auto-generated method stub
		Location location = new Location();
		location.setSupLocalCode(supLocalCode);
		return locationtMapper.getList(location);
	}

	public List<Location> getLocationByLvAndFlag(int lv,String flag){
		Location location = Location.builder().lv(lv).flag(flag).build();
//		location.setLv(lv);
//		location.setFlag(flag);
		return locationtMapper.getList(location);
	}

	public List<Location> findLocationTreeNodeByLocalCode(String localCode){
		return getLocationBySupLocalCode(localCode);
	}



	public List<Location> getLocationListByLevel(Integer lv){
		Location location = new Location();
		location.setLv(lv);
		return locationtMapper.getList(location);
	}

	public List<Location> getLocationListById(Long id){
		Location location = new Location();
		location.setId(id);
		return locationtMapper.getList(location);
	}



	@Autowired
	private LocationMapper locationtMapper;

	@Override
	protected BaseMapper<Location, Long> getMapper() {
		return locationtMapper;
	}

	public List<Location> getList2(Location location){
		return locationtMapper.getList2(location);
	}
}
