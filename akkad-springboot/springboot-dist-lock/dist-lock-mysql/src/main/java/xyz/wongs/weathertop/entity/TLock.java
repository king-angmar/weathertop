package xyz.wongs.weathertop.entity;

import lombok.Data;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;

import java.util.Date;

@Data
public class TLock extends BaseEntity<Integer> {

    private Integer id;

    private String lockKey;

    private String owner;

    private Integer expireSeconds;

    private Date createTime;


}