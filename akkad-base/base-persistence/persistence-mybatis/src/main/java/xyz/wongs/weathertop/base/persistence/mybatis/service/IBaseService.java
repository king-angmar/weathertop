package xyz.wongs.weathertop.base.persistence.mybatis.service;


import com.github.pagehelper.PageInfo;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;
import xyz.wongs.weathertop.base.persistence.mybatis.page.PaginationInfo;

import java.io.Serializable;

public interface IBaseService<T extends BaseEntity,ID extends Serializable>{

    PageInfo<T> selectPage(PaginationInfo pgInfo, T t);

    PageInfo<T> selectPageByCondition(PaginationInfo pgInfo, Object condition);

    PageInfo<T> selectByExample(PaginationInfo pgInfo, Object example);

    int deleteByPrimaryKey(ID id);
    Long insert(T t);
    Long insertSelective(T t);
    T selectByPrimaryKey(ID id);
    int updateByPrimaryKeySelective(T t);
    int updateByPrimaryKeyWithBLOBs(T t);
    int updateByPrimaryKey(T t);
}
