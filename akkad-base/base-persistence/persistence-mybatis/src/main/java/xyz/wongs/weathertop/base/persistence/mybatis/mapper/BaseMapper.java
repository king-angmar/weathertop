package xyz.wongs.weathertop.base.persistence.mybatis.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;
import xyz.wongs.weathertop.base.persistence.mybatis.page.PaginationInfo;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName BaseMapper
 * @Description 通用Mapper
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/11/21 17:40
 * @Version 1.0.0
*/
public interface BaseMapper<T extends BaseEntity,ID extends Serializable> {

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/7/23 19:27
     * @param t
     * @return 
     * @throws 
     * @since 
    */
    List<T> getList(T t);

    List<T> getListByCondition(Object condition);

    List<T> getListByExample(Object example);

    int deleteByPrimaryKey(ID id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(ID id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKeyWithBLOBs(T t);

    int updateByPrimaryKey(T t);

}
