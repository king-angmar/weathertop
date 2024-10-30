package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.system.entity.SysLoginInfo;
import xyz.wongs.weathertop.war3.system.mapper.SysLoginInfoMapper;

import java.util.List;


@Service
public class SysLoginInfoService extends BaseService<SysLoginInfo, Long> {

    @Autowired
    private SysLoginInfoMapper sysLoginInfoMapper;

    @Override
    protected BaseMapper<SysLoginInfo, Long> getMapper() {
        return sysLoginInfoMapper;
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLoginInfo> selectSysLoginInfoList(SysLoginInfo sysLoginInfo) {
        return sysLoginInfoMapper.selectSysLoginInfoList(sysLoginInfo);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    @Transactional(readOnly = false)
    public int deleteLoginInfoByIds(String ids) {
        return sysLoginInfoMapper.deleteLoginInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 清空系统登录日志
     */
    @Transactional(readOnly = false)
    public void cleanLoginInfo() {
        sysLoginInfoMapper.cleanLoginInfo();
    }
}