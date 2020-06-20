package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;
import xyz.wongs.weathertop.war3.system.mapper.SysUserOnlineMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class SysUserOnlineService extends BaseService<SysUserOnline, Long> {

    @Autowired
    private SysUserOnlineMapper sysUserOnlineMapper;

    public void deleteOnlineBySessionId(String id){

    }

    public SysUserOnline selectOnlineBySessionId(String id){
        return null;
    }

    public List<SysUserOnline> selectOnlineByExpired(Date date){
        return Collections.EMPTY_LIST;
    }

    public void batchDeleteOnline(List<String> needOfflineIdList){
    }

    @Override
    protected BaseMapper<SysUserOnline, Long> getMapper() {
        return sysUserOnlineMapper;
    }
}