package xyz.wongs.weathertop.base.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity<T> {

    private String id;
    private T data;
}
