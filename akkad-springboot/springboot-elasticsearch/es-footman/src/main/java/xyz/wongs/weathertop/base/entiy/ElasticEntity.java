package xyz.wongs.weathertop.base.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity<T> {

    private String id;
    private Map data;
}
