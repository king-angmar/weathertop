package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * @ClassName SysDictType
 * @Description 字典类型表(SysDictType)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:52
 * @Version 1.0.0
*/
@Data
public class SysDictType extends BaseEntity<Long> {
    /**
    * 字典主键
    */
    private Long id;
    /**
    * 字典名称
    */
    private String dictName;
    /**
    * 字典类型
    */
    private String dictType;
    /**
    * 状态（0正常 1停用）
    */
    private String status;
    /**
    * 创建者
    */
    private String createBy;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新者
    */
    private String updateBy;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 备注
    */
    private String remark;

}