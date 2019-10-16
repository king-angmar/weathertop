package xyz.wongs.weathertop.akkad.lsms.analydb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmNpdb extends BaseEntity<Long> {

    private Long id;

    private String portoutnetid;

    private String portinnetid;

    private String homenetid;

    private Date reqdatetime;


}