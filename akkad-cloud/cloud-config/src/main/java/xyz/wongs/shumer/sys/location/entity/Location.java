package xyz.wongs.shumer.sys.location.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.shumer.base.persistence.mybatis.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location extends BaseEntity<Long> {
    private Long id;

    private String flag;

    private String localCode;

    private String localName;

    private Integer lv;

    private String supLocalCode;

    private String url;
}