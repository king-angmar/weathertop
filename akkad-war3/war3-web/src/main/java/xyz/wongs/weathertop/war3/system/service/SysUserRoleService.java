package xyz.wongs.weathertop.war3.system.service;

import org.apache.bcel.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
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

    @Transactional
    public void updateUserRole(SysUser user){
        sysUserRoleMapper.deleteUserRoleByUserId(user.getId());
        this.insertUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Transactional
    public int insertAuthUsers(Long roleId, String userIds) {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : users) {
            list.add(buildSysUser(userId,roleId));
        }
        return batchInsert(list);
    }


    @Transactional
    public int batchInsert(List<SysUserRole> list){
        if (!list.isEmpty() && list.size() > 0)
            return this.batchUserRole(list);

        return 0;
    }

    public SysUserRole buildSysUser(Long userId,Long roleId){
        SysUserRole ur = new SysUserRole();
        ur.setId(super.getPrimaryKey(ur));
        ur.setUserId(userId);
        ur.setRoleId(roleId);
        return ur;
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
                list.add(buildSysUser(userId,roleId));
            }
            batchInsert(list);
        }
    }

    @Transactional
    public int batchUserRole(List<SysUserRole> userRoleList){
        return sysUserRoleMapper.batchUserRole(userRoleList);
    }
}