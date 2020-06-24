package xyz.wongs.weathertop.war3.system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.security.Md5Utils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.exception.user.BusinessException;
import xyz.wongs.weathertop.war3.system.entity.SysPost;
import xyz.wongs.weathertop.war3.system.entity.SysRole;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;
import xyz.wongs.weathertop.war3.system.mapper.SysPostMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysUserMapper;

import java.util.List;

/**
 * @author WCNGS@QQ.COM
 * @ClassName SysUserServiceImpl
 * @Description 用户信息表(SysUser)表服务实现类
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 15:31
 * @Version 1.0.0
 */
@Slf4j
@Service("sysUserService")
public class SysUserService extends BaseService<SysUser, Long> {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Transactional(readOnly = false)
    public Long insertUser(SysUser user) {
        // 新增用户信息
        Long id = sysUserMapper.insertSelective(user);
        user.setId(id);
        // 新增用户岗位关联
        sysUserPostService.insertUserPost(user);
        // 新增用户与角色管理
        sysUserRoleService.insertUserRole(id, user.getRoleIds());
        return id;
    }

    @Transactional(readOnly = false)
    public int updateUser(SysUser user) {
        int i = 1;
        try {
            // 新增用户信息
            sysUserMapper.updateByPrimaryKey(user);
            // 删除用户岗位关系，新增用户岗位关联
            sysUserPostService.updateUserPost(user);
            // 删除用户角色关系，新增用户与角色管理
            sysUserRoleService.updateUserRole(user);
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return sysUserMapper.selectUnallocatedList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user) {
        return sysUserMapper.selectAllocatedList(user);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserRoleGroup(Long userId) {
        List<SysRole> list = sysRoleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserPostGroup(Long userId) {
        List<SysPost> list = sysPostMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 用户状态修改
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional
    public int changeStatus(SysUser user) {
        return sysUserMapper.update(user);
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    public int deleteUserByIds(String ids) throws BusinessException {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        return sysUserMapper.deleteUserByIds(userIds);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        sysUserRoleService.deleteUserRoleByUserId(userId);
        sysUserRoleService.insertUserRole(userId, roleIds);
    }


    /**
     * 通过用户ID查询用户和角色关联
     *
     * @param userId 用户ID
     * @return 用户和角色关联列表
     */
    public List<SysUserRole> selectUserRoleByUserId(Long userId) {
        return sysUserMapper.selectUserRoleByUserId(userId);
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional
    public int resetUserPwd(SysUser user) {
        return updateUserInfo(user);
    }

    @Transactional
    public int updateUserInfo(SysUser user) {
        return sysUserMapper.update(user);
    }

    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin()) {
            throw new BusinessException("不允许操作超级管理员用户");
        }
    }

    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        SysUser info = sysUserMapper.checkPhoneUnique(user.getPhoneNumber());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = sysConfigService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = sysUserMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(Md5Utils.hash(user.getLoginName() + password));
                    user.setCreateBy(operName);
                    this.insert(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateByPrimaryKeySelective(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    public SysUser selectUserByLoginName(String userName) {
        return sysUserMapper.selectUserByLoginName(userName);
    }

    public SysUser selectUserByPhoneNumber(String phoneNumber) {
        return sysUserMapper.selectUserByPhoneNumber(phoneNumber);
    }

    public List<SysUser> selectUserList(SysUser user) {
        return sysUserMapper.selectUserList(user);
    }

    public SysUser selectUserByEmail(String email) {
        return sysUserMapper.selectUserByEmail(email);
    }

    public String checkLoginNameUnique(String loginName) {
        int count = sysUserMapper.checkLoginNameUnique(loginName);
        if (count > 0) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    @Transactional
    public boolean registerUser(SysUser sysUser) {
        sysUser.setUserType(UserConstants.REGISTER_USER_TYPE);
        return sysUserMapper.insertSelective(sysUser) > 0;
    }

    @Override
    protected BaseMapper<SysUser, Long> getMapper() {
        return sysUserMapper;
    }
}