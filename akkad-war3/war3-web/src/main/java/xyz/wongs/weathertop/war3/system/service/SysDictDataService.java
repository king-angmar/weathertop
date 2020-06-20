package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysDictData;
import xyz.wongs.weathertop.war3.system.mapper.SysDictDataMapper;

import java.util.List;


@Service
public class SysDictDataService extends BaseService<SysDictData, Long> {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    protected BaseMapper<SysDictData, Long> getMapper() {
        return sysDictDataMapper;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType){
        return sysDictDataMapper.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue)
    {
        return sysDictDataMapper.selectDictLabel(dictType, dictValue);
    }
}