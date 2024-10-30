package xyz.wongs.weathertop.base.persistence.mybatis.entity;

import java.io.Serializable;


public abstract class IdEntity<ID extends Serializable> implements Serializable {

    public abstract ID getId();

    public abstract void setId(ID id);
}
