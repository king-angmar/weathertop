package xyz.wongs.weathertop.shiro.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptRole extends BaseEntity<Long> {

    private Long id;

    private String roleName;

    private String roleCode;

    private Integer num;

    private Integer pid;

    private String tips;

    private Integer version;

    private String state;

  }