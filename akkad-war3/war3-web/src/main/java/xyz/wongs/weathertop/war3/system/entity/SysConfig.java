package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

/**
 * @ClassName SysConfig
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:47
 * @Version 1.0.0
*/
@Data
public class SysConfig extends BaseEntity<Long> {
    /**
    * 参数主键
    */
    private Long id;
    /**
    * 参数名称
    */
    private String configName;
    /**
    * 参数键名
    */
    private String configKey;
    /**
    * 参数键值
    */
    private String configValue;
    /**
    * 系统内置（Y是 N否）
    */
    private String configType;
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