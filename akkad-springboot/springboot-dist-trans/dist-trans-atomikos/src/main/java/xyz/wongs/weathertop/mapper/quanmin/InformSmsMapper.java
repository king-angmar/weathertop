package xyz.wongs.weathertop.mapper.quanmin;


import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.entity.quanmin.InformSms;
import xyz.wongs.weathertop.entity.quanmin.SysConfig;

public interface InformSmsMapper extends BaseMapper<InformSms,Long> {
    int deleteByPrimaryKey(Integer informId);

    Long insert(InformSms record);

    Long insertSelective(InformSms record);

    InformSms selectByPrimaryKey(Long informId);

    int updateByPrimaryKeySelective(InformSms record);

    int updateByPrimaryKey(InformSms record);
}