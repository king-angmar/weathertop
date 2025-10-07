package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysRoleMenu;
import xyz.wongs.weathertop.war3.system.mapper.SysRoleMenuMapper;


@Service
public class SysRoleMenuService extends BaseService<SysRoleMenu, Long> {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    protected BaseMapper<SysRoleMenu, Long> getMapper() {
        return sysRoleMenuMapper;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int selectCountRoleMenuByMenuId(Long menuId)
    {
        return sysRoleMenuMapper.selectCountRoleMenuByMenuId(menuId);
    }
}