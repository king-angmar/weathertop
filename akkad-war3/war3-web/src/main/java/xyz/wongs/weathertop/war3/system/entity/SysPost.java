package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 岗位信息表(SysPost)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:07
 */
@Data
public class SysPost extends BaseEntity<Long> {
    private static final long serialVersionUID = -23004720470501670L;
    /**
    * 岗位ID
    */
    private Long id;
    /**
    * 岗位编码
    */
    private String postCode;
    /**
    * 岗位名称
    */
    private String postName;
    /**
    * 显示顺序
    */
    private Integer postSort;
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

    /** 用户是否存在此岗位标识 默认不存在 */
    private boolean flag = false;


}