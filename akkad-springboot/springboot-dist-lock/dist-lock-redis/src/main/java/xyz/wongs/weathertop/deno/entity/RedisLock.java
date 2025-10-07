package xyz.wongs.weathertop.deno.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisLock extends BaseEntity<Integer> {
    private Integer id;
    private Integer counts;

}