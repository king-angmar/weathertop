package xyz.wongs.weathertop.shiro.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptAcctRolRef extends BaseEntity<Long> {
    private Long id;

    private Long roleId;

    private Integer accountid;

    private String state;
}