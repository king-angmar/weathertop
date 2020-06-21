package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.system.entity.SysOperLog;
import xyz.wongs.weathertop.war3.system.mapper.SysOperLogMapper;

import java.util.List;


@Service
public class SysOperLogService extends BaseService<SysOperLog, Long> {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Override
    protected BaseMapper<SysOperLog, Long> getMapper() {
        return sysOperLogMapper;
    }

    /**
     * 清空操作日志
     */
    public void cleanOperLog() {
        sysOperLogMapper.cleanOperLog();
    }

    /**
     * 批量删除系统操作日志
     *
     * @param ids 需要删除的数据
     * @return
     */
    public int deleteOperLogByIds(String ids) {
        return sysOperLogMapper.deleteOperLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
        return sysOperLogMapper.selectOperLogList(operLog);
    }
}