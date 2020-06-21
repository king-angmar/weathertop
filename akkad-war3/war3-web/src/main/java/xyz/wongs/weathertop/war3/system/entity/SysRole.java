package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 角色信息表(SysRole)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:07
 */
@Data
public class SysRole extends BaseEntity<Long> {

    public SysRole(){}

    public SysRole(Long id){
        this.id = id ;
    }
    /**
    * 角色ID
    */
    private Long id;
    /**
    * 角色名称
    */
    private String roleName;
    /**
    * 角色权限字符串
    */
    private String roleKey;
    /**
    * 显示顺序
    */
    private Integer roleSort;
    /**
    * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    */
    private String dataScope;
    /**
    * 角色状态（0正常 1停用）
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
    /**
    * 备注
    */
    private String remark;

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 部门组（数据权限） */
    private Long[] deptIds;

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

}