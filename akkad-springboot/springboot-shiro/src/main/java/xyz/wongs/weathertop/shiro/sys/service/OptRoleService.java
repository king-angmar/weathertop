package xyz.wongs.weathertop.shiro.sys.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.shiro.sys.entity.OptRole;
import xyz.wongs.weathertop.shiro.sys.entity.SAccount;
import xyz.wongs.weathertop.shiro.sys.entity.SAccountExample;
import xyz.wongs.weathertop.shiro.sys.mapper.OptRoleMapper;
import xyz.wongs.weathertop.shiro.sys.mapper.SAccountMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Transactional(readOnly = true)
@Service
public class OptRoleService extends BaseService<OptRole, Long> {

    @Autowired
    private OptRoleMapper optRoleMapper;


    @Override
    protected BaseMapper<OptRole, Long> getMapper() {
        return optRoleMapper;
    }

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/24 16:00
     * @param accountid
     * @return java.util.List<xyz.wongs.weathertop.shiro.sys.entity.OptRole>
     * @throws
     * @since
     */
    public List<OptRole> selectRoleByAcctId(Integer accountid){
        Map<String,Object> map = new HashMap<>();
        map.put("accountid",accountid);
        map.put("state","70A");
        return optRoleMapper.selectRoleByAcctId(map);
    }

}
