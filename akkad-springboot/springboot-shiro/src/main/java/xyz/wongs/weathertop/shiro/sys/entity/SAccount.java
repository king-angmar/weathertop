package xyz.wongs.weathertop.shiro.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SAccount extends BaseEntity<Integer> {
    private Integer id;

    private String accountcode;

    private String password;

    private String accountname;

    private String state;

    private Date createtime;

    private Date modifytime;

    private Date overdate;

    private Date disableddate;

    private String remark;

}