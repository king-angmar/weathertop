package xyz.wongs.weathertop.entity.quanmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig{
    private Long id;

    private String key;

    private String value;

    private Byte status;

    private String remark;

    private Date gmtCreate;

    private Date gmtModified;


}