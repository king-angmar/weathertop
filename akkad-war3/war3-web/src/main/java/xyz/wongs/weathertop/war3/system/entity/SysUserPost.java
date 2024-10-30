package xyz.wongs.weathertop.war3.system.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

/**
 * 用户与岗位关联表(SysUserPost)实体类
 *
 * @author makejava
 * @since 2020-06-19 15:50:08
 */
@Data
public class SysUserPost extends BaseEntity<Long> {

    private Long id;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 岗位ID
    */
    private Long postId;

}