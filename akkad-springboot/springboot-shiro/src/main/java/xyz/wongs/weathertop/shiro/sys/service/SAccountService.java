package xyz.wongs.weathertop.shiro.sys.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.shiro.sys.entity.SAccount;
import xyz.wongs.weathertop.shiro.sys.entity.SAccountExample;
import xyz.wongs.weathertop.shiro.sys.mapper.SAccountMapper;

import java.util.List;


@Slf4j
@Transactional(readOnly = true)
@Service
public class SAccountService extends BaseService<SAccount, Integer> {

    @Autowired
    private SAccountMapper sAccountMapper;


    @Override
    protected BaseMapper<SAccount, Integer> getMapper() {
        return sAccountMapper;
    }

    public List<SAccount> selectByExample(SAccount sAccount){
        SAccountExample example =new SAccountExample();
        SAccountExample.Criteria criteria = example.createCriteria();
        example.setDistinct(false);
        if(!StringUtils.isNotBlank(sAccount.getAccountname())){
            criteria.andAccountnameEqualTo(sAccount.getAccountname());
        }
        return sAccountMapper.selectByExample(example);
    }

}
