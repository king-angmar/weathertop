package xyz.wongs.weathertop.shiro.sys.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.shiro.sys.entity.OptMenu;
import xyz.wongs.weathertop.shiro.sys.entity.OptRole;
import xyz.wongs.weathertop.shiro.sys.mapper.OptMenuMapper;
import xyz.wongs.weathertop.shiro.sys.mapper.OptRoleMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Transactional(readOnly = true)
@Service
public class OptMenuService extends BaseService<OptMenu, Long> {

    @Autowired
    private OptMenuMapper optMenuMapper;

    @Override
    protected BaseMapper<OptMenu, Long> getMapper() {
        return optMenuMapper;
    }

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/24 16:18
     * @param roleId
     * @return java.util.List<xyz.wongs.weathertop.shiro.sys.entity.OptMenu>
     * @throws
     * @since
     */
    public List<OptMenu> selectMenuByRoleId(Long roleId){
        Map map = new HashMap<>();
        map.put("roleId",roleId);
        map.put("state","70A");
        return optMenuMapper.selectMenuByRoleId(map);
    }
}
