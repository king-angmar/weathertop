package xyz.wongs.weathertop.base.persistence.mybatis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.wongs.weathertop.base.persistence.mybatis.config.Global;
import xyz.wongs.weathertop.base.persistence.mybatis.stas.Cons;

import java.io.Serializable;

public abstract class BaseEntity<ID extends Serializable> extends IdEntity<ID> {

    @JSONField
    private String dtype;

    public String getDtype() {
        return Global.getConfig(Cons.DB_TYPE);
    }


}
