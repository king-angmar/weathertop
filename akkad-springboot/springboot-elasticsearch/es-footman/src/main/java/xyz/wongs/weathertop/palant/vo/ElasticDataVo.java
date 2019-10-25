package xyz.wongs.weathertop.palant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.entiy.ElasticEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticDataVo<T> {

    /**
     * idxName : idx_location
     * elasticEntity : {"id":1,"location":{"location_id":"143831","flag":"Y","local_code":"11","local_name":"市","lv":2,"sup_local_code":"0","url":"11.html"}}
     */

    private String idxName;
    private ElasticEntity elasticEntity;

}
