package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;
import xyz.wongs.weathertop.war3.system.mapper.SysUserOnlineMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class SysUserOnlineService extends BaseService<SysUserOnline, Long> {

    @Autowired
    private SysUserOnlineMapper sysUserOnlineMapper;

    /**
     * @param id
     * @return void
     * @throws
     * @Description 通过会话序号删除信息
     * @date 2020/6/20 22:55
     */
    @Transactional(readOnly = false)
    public void deleteOnlineBySessionId(String sessionId) {
        SysUserOnline userOnline = selectOnlineBySessionId(sessionId);
        if (StringUtils.isNotNull(userOnline)) {
            sysUserOnlineMapper.deleteOnlineBySessionId(sessionId);
        }
    }
    /**
     * 查询会话集合
     *
     * @param userOnline 在线用户
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline) {
        return sysUserOnlineMapper.selectUserOnlineList(userOnline);
    }

    public SysUserOnline selectOnlineBySessionId(String sessionId) {
        return sysUserOnlineMapper.selectOnlineBySessionId(sessionId);
    }

    public List<SysUserOnline> selectOnlineByExpired(Date date) {
        return Collections.EMPTY_LIST;
    }

    @Transactional(readOnly = false)
    public void batchDeleteOnline(List<String> sessions) {
        sysUserOnlineMapper.deleteOnlineBySessionIds(sessions.toArray(new String[sessions.size()]));
    }

    @Override
    protected BaseMapper<SysUserOnline, Long> getMapper() {
        return sysUserOnlineMapper;
    }
}