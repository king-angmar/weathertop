package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;
import xyz.wongs.weathertop.war3.system.mapper.SysUserRoleMapper;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysUserRoleService extends BaseService<SysUserRole, Long> {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    protected BaseMapper<SysUserRole, Long> getMapper() {
        return sysUserRoleMapper;
    }

    @Transactional
    public int deleteUserRoleByUserId(Long userId){
        return sysUserRoleMapper.deleteUserRoleByUserId(userId);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    @Transactional
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotNull(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setId(super.getPrimaryKey(ur));
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                this.batchUserRole(list);
            }
        }
    }

    @Transactional
    public int batchUserRole(List<SysUserRole> userRoleList){
        return sysUserRoleMapper.batchUserRole(userRoleList);
    }
}