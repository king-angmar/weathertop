package xyz.wongs.weathertop.entity.quanmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformSms extends BaseEntity<Long> {

    private Long id;

    private String serviceType;

    private String serviceId;

    private String content;

    private Date trigerTime;

    private Date createDate;

    private String state;

    private String stateRemark;

    private Date stateDate;

}