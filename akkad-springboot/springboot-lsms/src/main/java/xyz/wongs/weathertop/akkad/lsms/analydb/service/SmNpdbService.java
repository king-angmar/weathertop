package xyz.wongs.weathertop.akkad.lsms.analydb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.akkad.lsms.analydb.entity.SmNpdb;
import xyz.wongs.weathertop.akkad.lsms.analydb.mapper.SmNpdbMapper;
import xyz.wongs.weathertop.akkad.lsms.common.TaskRuntimeException;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;

import java.util.Date;
import java.util.List;

/**
 * @Author linwei
 * @Date 2019/8/9
 **/
@Slf4j
@Transactional(readOnly = true)
@Service
public class SmNpdbService extends BaseService<SmNpdb, Long> {
    @Autowired
    private SmNpdbMapper smNpdbMapper;

    @Override
    protected BaseMapper<SmNpdb, Long> getMapper() {
        return smNpdbMapper;
    }

    @Transactional(readOnly = false)
    public int insertBatchByOn(List<SmNpdb> list){
        int i = 0;
        try {
            i = smNpdbMapper.insertBatchByOn(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TaskRuntimeException(" 入库失败");
        }
        return i;
    }
}
