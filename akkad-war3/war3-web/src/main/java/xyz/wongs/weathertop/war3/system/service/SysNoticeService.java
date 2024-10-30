package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.system.entity.SysNotice;
import xyz.wongs.weathertop.war3.system.mapper.SysNoticeMapper;

import java.util.List;


@Service
public class SysNoticeService extends BaseService<SysNotice, Long> {

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    @Override
    protected BaseMapper<SysNotice, Long> getMapper() {
        return sysNoticeMapper;
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        return sysNoticeMapper.selectNoticeList(notice);
    }

    /**
     * 删除公告对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNoticeByIds(String ids) {
        return sysNoticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
    }
}