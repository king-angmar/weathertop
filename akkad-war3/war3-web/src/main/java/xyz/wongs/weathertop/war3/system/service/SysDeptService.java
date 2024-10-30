package xyz.wongs.weathertop.war3.system.service;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.domain.Ztree;
import xyz.wongs.weathertop.war3.system.entity.SysDept;
import xyz.wongs.weathertop.war3.system.entity.SysRole;
import xyz.wongs.weathertop.war3.system.mapper.SysDeptMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class SysDeptService extends BaseService<SysDept, Long> {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    protected BaseMapper<SysDept, Long> getMapper() {
        return sysDeptMapper;
    }

    public List<SysDept> selectDeptList(SysDept dept) {
        return sysDeptMapper.selectDeptList(dept);
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getId()) ? -1L : dept.getId();
        SysDept info = sysDeptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != deptId.longValue()) {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }

    /**
     * 查询部门人数
     *
     * @param parentId 部门ID
     * @return 结果
     */
    public int selectDeptCount(Long parentId) {
        SysDept dept = new SysDept();
        dept.setParentId(parentId);
        return sysDeptMapper.selectDeptCount(dept);
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId) {
        int result = sysDeptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTree(SysDept dept) {
        List<SysDept> deptList = sysDeptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 查询部门管理树（排除下级）
     *
     * @param deptId 部门ID
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTreeExcludeChild(SysDept dept) {
        Long deptId = dept.getId();
        List<SysDept> deptList = sysDeptMapper.selectDeptList(dept);
        Iterator<SysDept> it = deptList.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 根据角色ID查询部门（数据权限）
     *
     * @param role 角色对象
     * @return 部门列表（数据权限）
     */
    public List<Ztree> roleDeptTreeData(SysRole role) {
        Long roleId = role.getId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDept> deptList = selectDeptList(new SysDept());
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleDeptList = sysDeptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        } else {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId) {
        return sysDeptMapper.selectNormalChildrenDeptById(deptId);
    }
}