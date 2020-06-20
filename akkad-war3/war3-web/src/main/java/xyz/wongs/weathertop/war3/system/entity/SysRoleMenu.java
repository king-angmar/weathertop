package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

/**
 * 角色和菜单关联表(SysRoleMenu)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:07
 */
@Data
public class SysRoleMenu extends BaseEntity<Long> {
    private Long id;
    /**
    * 角色ID
    */
    private Long roleId;
    /**
    * 菜单ID
    */
    private Long menuId;
}