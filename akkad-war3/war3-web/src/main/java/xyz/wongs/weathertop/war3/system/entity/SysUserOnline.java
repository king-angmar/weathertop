package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 在线用户记录(SysUserOnline)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:08
 */
@Data
public class SysUserOnline extends BaseEntity<Long> {
    private static final long serialVersionUID = 720127322756933605L;

    private Long id;
    /**
    * 用户会话id
    */
    private String sessionId;
    /**
    * 登录账号
    */
    private String loginName;
    /**
    * 部门名称
    */
    private String deptName;
    /**
    * 登录IP地址
    */
    private String ipaddr;
    /**
    * 登录地点
    */
    private String loginLocation;
    /**
    * 浏览器类型
    */
    private String browser;
    /**
    * 操作系统
    */
    private String os;
    /**
    * 在线状态on_line在线off_line离线
    */
    private String status;
    /**
    * session创建时间
    */
    private Date startTimestamp;
    /**
    * session最后访问时间
    */
    private Date lastAccessTime;
    /**
    * 超时时间，单位为分钟
    */
    private Long expireTime;

}