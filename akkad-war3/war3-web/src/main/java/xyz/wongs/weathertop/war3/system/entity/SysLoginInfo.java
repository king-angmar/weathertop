package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * @ClassName SysLogininfor
 * @Description 系统访问记录(SysLogininfor)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:54
 * @Version 1.0.0
*/
@Data
public class SysLoginInfo extends BaseEntity<Long> {
    /**
    * 访问ID
    */
    private Long id;
    /**
    * 登录账号
    */
    private String loginName;
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
    * 登录状态（0成功 1失败）
    */
    private String status;
    /**
    * 提示消息
    */
    private String msg;
    /**
    * 访问时间
    */
    private Date loginTime;

}