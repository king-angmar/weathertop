package xyz.wongs.weathertop.entity.quanmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends BaseEntity<Long> {
    private Long id;

    private String key;

    private String value;

    private int status;

    private String remark;

    private Date gmtCreate;

    private Date gmtModified;


}