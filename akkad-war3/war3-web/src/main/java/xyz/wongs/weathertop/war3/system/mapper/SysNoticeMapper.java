package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysNotice;

import java.util.List;

@MyBatisMapper
public interface SysNoticeMapper extends BaseMapper<SysNotice,Long> {
    int deleteByPrimaryKey(Integer noticeId);

    Long insert(SysNotice record);

    Long insertSelective(SysNotice record);

    SysNotice selectByPrimaryKey(Integer noticeId);

    int updateByPrimaryKeySelective(SysNotice record);

    int updateByPrimaryKey(SysNotice record);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 批量删除公告
     *
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    int deleteNoticeByIds(String[] noticeIds);
}