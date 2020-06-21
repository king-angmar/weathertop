package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.domain.Ztree;
import xyz.wongs.weathertop.war3.common.utils.DictUtils;
import xyz.wongs.weathertop.war3.exception.user.BusinessException;
import xyz.wongs.weathertop.war3.system.entity.SysDictData;
import xyz.wongs.weathertop.war3.system.entity.SysDictType;
import xyz.wongs.weathertop.war3.system.mapper.SysDictDataMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysDictTypeMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysDictTypeService extends BaseService<SysDictType, Long> {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    protected BaseMapper<SysDictType, Long> getMapper() {
        return sysDictTypeMapper;
    }

    /**
     * 查询字典类型树
     *
     * @param dictType 字典类型
     * @return 所有字典类型
     */
    public List<Ztree> selectDictTree(SysDictType dictType) {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDictType> dictList = sysDictTypeMapper.selectDictTypeList(dictType);
        for (SysDictType dict : dictList) {
            if (UserConstants.DICT_NORMAL.equals(dict.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(dict.getId());
                ztree.setName(transDictName(dict));
                ztree.setTitle(dict.getDictType());
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    public String transDictName(SysDictType dictType) {
        StringBuffer sb = new StringBuffer();
        sb.append("(" + dictType.getDictName() + ")");
        sb.append("&nbsp;&nbsp;&nbsp;" + dictType.getDictType());
        return sb.toString();
    }

    /**
     * 清空缓存数据
     */
    public void clearCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    public String checkDictTypeUnique(SysDictType dict) {
        Long dictId = StringUtils.isNull(dict.getId()) ? -1L : dict.getId();
        SysDictType dictType = sysDictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getId().longValue() != dictId.longValue()) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public SysDictType selectDictTypeById(Long dictId) {
        return sysDictTypeMapper.selectByPrimaryKey(dictId);
    }

    /**
     * 批量删除字典类型
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int deleteDictTypeByIds(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (sysDictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }
        int count = sysDictTypeMapper.deleteDictTypeByIds(dictIds);
        if (count > 0) {
            DictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Transactional
    public int updateDictType(SysDictType dictType) {
        SysDictType oldDict = sysDictTypeMapper.selectByPrimaryKey(dictType.getId());
        sysDictTypeMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        int row = sysDictTypeMapper.updateByPrimaryKey(dictType);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        List<SysDictType> dictTypeList = sysDictTypeMapper.selectDictTypeAll();
        for (SysDictType dictType : dictTypeList) {
            List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    public SysDictType selectDictTypeByType(String dictType) {
        return sysDictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll() {
        return sysDictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotNull(dictDatas)) {
            return dictDatas;
        }
        dictDatas = sysDictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotNull(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return sysDictTypeMapper.selectDictTypeList(dictType);
    }
}