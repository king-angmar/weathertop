package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysNotice;

@MyBatisMapper
public interface SysNoticeMapper extends BaseMapper<SysNotice,Long> {
    int deleteByPrimaryKey(Integer noticeId);

    Long insert(SysNotice record);

    Long insertSelective(SysNotice record);

    SysNotice selectByPrimaryKey(Integer noticeId);

    int updateByPrimaryKeySelective(SysNotice record);

    int updateByPrimaryKey(SysNotice record);
}