package xyz.wongs.weathertop.war3.system.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;

import java.util.List;

@MyBatisMapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole,Long> {
    int deleteByPrimaryKey(Long userRoleId);

    Long insert(SysUserRole record);

    Long insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long userRoleId);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);


    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countUserRoleByRoleId(Long roleId);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserRoleByUserId(Long userId);
}