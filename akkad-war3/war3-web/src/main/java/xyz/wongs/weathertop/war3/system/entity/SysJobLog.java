package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * @ClassName SysJobLog
 * @Description 定时任务调度日志表(SysJobLog)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:54
 * @Version 1.0.0
*/
@Data
public class SysJobLog  extends BaseEntity<Long> {
    /**
    * 任务日志ID
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
    * 日志信息
    */
    private String jobMessage;
    /**
    * 执行状态（0正常 1失败）
    */
    private String status;
    /**
    * 异常信息
    */
    private String exceptionInfo;
    /**
    * 创建时间
    */
    private Date createTime;

}