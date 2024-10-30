package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

/**
 * 用户和角色关联表(SysUserRole)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:08
 */
@Data
public class SysUserRole extends BaseEntity<Long> {

    private Long id;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 角色ID
    */
    private Long roleId;

}