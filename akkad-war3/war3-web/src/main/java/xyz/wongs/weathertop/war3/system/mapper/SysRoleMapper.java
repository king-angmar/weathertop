package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysRole;

import java.util.List;

@MyBatisMapper
public interface SysRoleMapper extends BaseMapper<SysRole,Long> {
    int deleteByPrimaryKey(Long roleId);

    Long insert(SysRole record);

    Long insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 批量角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] ids);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    SysRole checkRoleKeyUnique(String roleKey);
}