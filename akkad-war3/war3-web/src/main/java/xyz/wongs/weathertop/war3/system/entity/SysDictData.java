package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;

import java.util.Date;
import java.io.Serializable;

/**
 * @author WCNGS@QQ.COM
 * @ClassName SysDictData
 * @Description 字典数据表(SysDictData)实体类
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:51
 * @Version 1.0.0
 */
@Data
public class SysDictData extends BaseEntity<Long> {
    /**
     * 字典编码
     */
    private Long id;
    /**
     * 字典排序
     */
    private Integer dictSort;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典键值
     */
    private String dictValue;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;
    /**
     * 表格回显样式
     */
    private String listClass;
    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;
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

    public boolean getDefault() {
        return UserConstants.YES.equals(this.isDefault) ? true : false;
    }
}