package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * @ClassName SysJob
 * @Description 定时任务调度表(SysJob)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:53
 * @Version 1.0.0
*/
@Data
public class SysJob  extends BaseEntity<Long> {
    /**
    * 任务ID
    */
    private Long id;
    /**
    * 任务名称
    */
    private String jobName;
    /**
    * 任务组名
    */
    private String jobGroup;
    /**
    * 调用目标字符串
    */
    private String invokeTarget;
    /**
    * cron执行表达式
    */
    private String cronExpression;
    /**
    * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
    */
    private String misfirePolicy;
    /**
    * 是否并发执行（0允许 1禁止）
    */
    private String concurrent;
    /**
    * 状态（0正常 1暂停）
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
    * 备注信息
    */
    private String remark;

}