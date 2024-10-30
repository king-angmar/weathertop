package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.SpringContextHolder;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.exception.user.BusinessException;
import xyz.wongs.weathertop.war3.system.entity.SysMenu;
import xyz.wongs.weathertop.war3.system.entity.SysRole;
import xyz.wongs.weathertop.war3.system.entity.SysRoleDept;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;
import xyz.wongs.weathertop.war3.system.mapper.SysMenuMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleDeptMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysUserRoleMapper;

import java.util.*;

/**
 * @author WCNGS@QQ.COM
 * @ClassName SysRoleService
 * @Description
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:31
 * @Version 1.0.0
 */
@Service
public class SysRoleService extends BaseService<SysRole, Long> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;


    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Transactional
    public int deleteAuthUser(SysUserRole userRole) {
        return sysUserRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        sysRoleMapper.updateByPrimaryKey(role);
        // 删除角色与部门关联
        sysRoleDeptMapper.deleteRoleDeptByRoleId(role.getId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = sysRoleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        SysRole info = sysRoleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId) {
        return sysUserRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    public int deleteRoleByIds(String ids) throws BusinessException {
        Long[] roleIds = Convert.toLongArray(ids);
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return sysRoleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Transactional
    public int deleteAuthUsers(Long roleId, String userIds) {
        return sysUserRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    /**
     * 角色状态修改
     *
     * @param role 角色信息
     * @return 结果
     */
    public int changeStatus(SysRole role) {
        return sysRoleMapper.updateByPrimaryKey(role);
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        SysRole info = sysRoleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getId()) && role.isAdmin()) {
            throw new BusinessException("不允许操作超级管理员角色");
        }
    }

    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = sysRoleMapper.selectRolesByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getId().longValue() == userRole.getId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    public Set<String> selectRoleKeys(Long userId) {
        List<SysRole> perms = sysRoleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    public List<SysRole> selectRoleAll() {
        return selectRoleList(new SysRole());
    }

    public List<SysRole> selectRoleList(SysRole role) {
        return sysRoleMapper.selectRoleList(role);
    }

    @Override
    protected BaseMapper<SysRole, Long> getMapper() {
        return sysRoleMapper;
    }
}