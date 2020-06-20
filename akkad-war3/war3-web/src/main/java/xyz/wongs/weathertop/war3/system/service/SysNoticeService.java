package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysNotice;
import xyz.wongs.weathertop.war3.system.mapper.SysNoticeMapper;


@Service
public class SysNoticeService extends BaseService<SysNotice, Long> {

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    @Override
    protected BaseMapper<SysNotice, Long> getMapper() {
        return sysNoticeMapper;
    }
}