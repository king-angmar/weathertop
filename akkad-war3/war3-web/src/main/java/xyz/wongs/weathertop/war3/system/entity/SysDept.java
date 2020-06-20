package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * @ClassName SysDept
 * @Description 部门表(SysDept)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:50
 * @Version 1.0.0
*/
@Data
public class SysDept extends BaseEntity<Long> {
    /**
    * 部门id
    */
    private Long id;
    /**
    * 父部门id
    */
    private Long parentId;
    /**
    * 祖级列表
    */
    private String ancestors;
    /**
    * 部门名称
    */
    private String deptName;
    /**
    * 显示顺序
    */
    private Integer orderNum;
    /**
    * 负责人
    */
    private String leader;
    /**
    * 联系电话
    */
    private String phone;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 部门状态（0正常 1停用）
    */
    private String status;
    /**
    * 删除标志（0代表存在 2代表删除）
    */
    private String delFlag;
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

}