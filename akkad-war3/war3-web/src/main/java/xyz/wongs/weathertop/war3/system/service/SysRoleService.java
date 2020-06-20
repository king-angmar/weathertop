package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.SpringContextHolder;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.system.entity.SysMenu;
import xyz.wongs.weathertop.war3.system.entity.SysRole;
import xyz.wongs.weathertop.war3.system.mapper.SysMenuMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleMapper;

import java.util.*;

/**
 * @ClassName SysRoleService
 * @Description
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:31
 * @Version 1.0.0
*/
@Service
public class SysRoleService extends BaseService<SysRole, Long> {

    @Autowired
    private SysRoleMapper sysRoleMapper;


    public List<SysRole> selectRolesByUserId(Long userId)
    {
        List<SysRole> userRoles = sysRoleMapper.selectRolesByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getId().longValue() == userRole.getId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    public Set<String> selectRoleKeys(Long userId){
        List<SysRole> perms = sysRoleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    public List<SysRole> selectRoleAll(){
        return selectRoleList(new SysRole());
    }

    public List<SysRole> selectRoleList(SysRole role)
    {
        return sysRoleMapper.selectRoleList(role);
    }

    @Override
    protected BaseMapper<SysRole, Long> getMapper() {
        return sysRoleMapper;
    }
}