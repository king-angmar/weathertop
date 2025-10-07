package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * @ClassName SysOperLog
 * @Description 操作日志记录(SysOperLog)实体类
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:56
 * @Version 1.0.0
*/
@Data
public class SysOperLog extends BaseEntity<Long> {
    private static final long serialVersionUID = -19786970488394729L;
    /**
    * 日志主键
    */
    private Long id;
    /**
    * 模块标题
    */
    private String title;
    /**
    * 业务类型（0其它 1新增 2修改 3删除）
    */
    private Integer businessType;

    /** 业务类型数组 */
    private Integer[] businessTypes;

    /**
    * 方法名称
    */
    private String method;
    /**
    * 请求方式
    */
    private String requestMethod;
    /**
    * 操作类别（0其它 1后台用户 2手机端用户）
    */
    private Integer operatorType;
    /**
    * 操作人员
    */
    private String operName;
    /**
    * 部门名称
    */
    private String deptName;
    /**
    * 请求URL
    */
    private String operUrl;
    /**
    * 主机地址
    */
    private String operIp;
    /**
    * 操作地点
    */
    private String operLocation;
    /**
    * 请求参数
    */
    private String operParam;
    /**
    * 返回参数
    */
    private String jsonResult;
    /**
    * 操作状态（0正常 1异常）
    */
    private Integer status;
    /**
    * 错误消息
    */
    private String errorMsg;
    /**
    * 操作时间
    */
    private Date operTime;

}