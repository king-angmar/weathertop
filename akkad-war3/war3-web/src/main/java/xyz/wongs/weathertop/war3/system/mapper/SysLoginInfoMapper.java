package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysLoginInfo;

import java.util.List;

@MyBatisMapper
public interface SysLoginInfoMapper extends BaseMapper<SysLoginInfo,Long> {

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<SysLoginInfo> selectSysLoginInfoList(SysLoginInfo sysLoginInfo);

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteLoginInfoByIds(String[] ids);

    /**
     * 清空系统登录日志
     *
     * @return 结果
     */
    int cleanLoginInfo();

    int deleteByPrimaryKey(Long infoId);

    Long insert(SysLoginInfo record);

    Long insertSelective(SysLoginInfo record);

    SysLoginInfo selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(SysLoginInfo record);

    int updateByPrimaryKey(SysLoginInfo record);
}