package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * 通知公告表(SysNotice)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:07
 */
@Data
public class SysNotice  extends BaseEntity<Long> {
    /**
    * 公告ID
    */
    private Long id;
    /**
    * 公告标题
    */
    private String noticeTitle;
    /**
    * 公告类型（1通知 2公告）
    */
    private String noticeType;
    /**
    * 公告内容
    */
    private String noticeContent;
    /**
    * 公告状态（0正常 1关闭）
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