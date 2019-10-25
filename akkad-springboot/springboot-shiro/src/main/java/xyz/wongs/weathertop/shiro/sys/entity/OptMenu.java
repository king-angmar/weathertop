package xyz.wongs.weathertop.shiro.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.wongs.weathertop.base.persistence.mybatis.entity.BaseEntity;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptMenu extends BaseEntity<Long> {

    private Long id;

    private String menuName;

    private String menuCode;

    private String pId;

    private String pcodes;

    private String icon;

    private String url;

    private Integer num;

    private Integer menuLevel;

    private Integer isMenu;

    private String tips;

    private String state;

    private Integer isOpen;
}