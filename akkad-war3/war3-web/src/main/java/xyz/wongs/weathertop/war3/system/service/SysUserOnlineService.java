package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;
import xyz.wongs.weathertop.war3.system.mapper.SysUserOnlineMapper;

import java.util.*;


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
        List<SysUserOnline> userOnlines = selectOnlineBySessionId(sessionId);
        if (!userOnlines.isEmpty()) {
//            sysUserOnlineMapper.deleteOnlineBySessionId(sessionId);
            List<String> sessionIds = getSeesionId(userOnlines);
            batchDeleteOnline(sessionIds);
        }
    }

    public List<String> getSeesionId(List<SysUserOnline> userOnlines){
        List<String> list = new ArrayList<String>(userOnlines.size());
        Iterator<SysUserOnline> iterator = userOnlines.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getSessionId());
        }
        return list;
    }
    /**
     * 查询会话集合
     *
     * @param userOnline 在线用户
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline) {
        return sysUserOnlineMapper.selectUserOnlineList(userOnline);
    }

    public List<SysUserOnline> selectOnlineBySessionId(String sessionId) {
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