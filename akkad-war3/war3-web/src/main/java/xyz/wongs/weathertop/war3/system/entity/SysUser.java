package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;
import xyz.wongs.weathertop.war3.common.annotation.Excel;
import xyz.wongs.weathertop.war3.common.annotation.Excel.Type;
import xyz.wongs.weathertop.war3.common.annotation.Excels;

import java.util.Date;
import java.util.List;

/**
 * 用户信息表(SysUser)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:07
 */
@Data
public class SysUser extends BaseEntity<Long> {

    public SysUser() {
    }

    public SysUser(Long id) {
        this.id = id;
    }

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门父ID
     */
    private Long parentId;
    /**
     * 登录账号
     */
    private String loginName;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户类型（00系统用户 01注册用户）
     */
    private String userType;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;
    /**
     * 头像路径
     */
    private String avatar;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐加密
     */
    private String salt;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 最后登陆IP
     */
    private String loginIp;
    /**
     * 最后登陆时间
     */
    private Date loginDate;
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

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private List<SysRole> roles;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long id) {
        return id != null && 1L == id;
    }

    private SysDept dept;

    public SysDept getDept() {
        if (dept == null) {
            dept = new SysDept();
        }
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

}