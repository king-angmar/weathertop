package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysRoleDept;

import java.util.List;

@MyBatisMapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept,Long> {
    int deleteByPrimaryKey(Long roleDeptId);

    Long insert(SysRoleDept record);

    Long insertSelective(SysRoleDept record);

    SysRoleDept selectByPrimaryKey(Long roleDeptId);

    int updateByPrimaryKeySelective(SysRoleDept record);

    int updateByPrimaryKey(SysRoleDept record);

    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    int batchRoleDept(List<SysRoleDept> roleDeptList);
}