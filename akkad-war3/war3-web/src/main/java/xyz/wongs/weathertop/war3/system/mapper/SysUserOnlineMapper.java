package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;

import java.util.List;

@MyBatisMapper
public interface SysUserOnlineMapper extends BaseMapper<SysUserOnline,Long> {

    /**
     * 查询会话集合
     *
     * @param userOnline 会话参数
     * @return 会话集合
     */
    List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline);

    /**
     * 通过会话序号删除信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    int deleteOnlineBySessionId(String sessionId);


    int deleteOnlineBySessionIds(String[] ids);

    /**
     * 通过会话序号查询信息
     *
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    List<SysUserOnline> selectOnlineBySessionId(String sessionId);

    int deleteByPrimaryKey(String sessionId);

    Long insert(SysUserOnline record);

    Long insertSelective(SysUserOnline record);

    SysUserOnline selectByPrimaryKey(String userOnlineId);

    int updateByPrimaryKeySelective(SysUserOnline record);

    int updateByPrimaryKey(SysUserOnline record);
}