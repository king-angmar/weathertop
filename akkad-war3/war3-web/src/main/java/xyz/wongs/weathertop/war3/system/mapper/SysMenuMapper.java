package xyz.wongs.weathertop.war3.system.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysMenu;

import java.util.List;

@MyBatisMapper
public interface SysMenuMapper extends BaseMapper<SysMenu,Long> {

    int deleteByPrimaryKey(Long menuId);

    Long insert(SysMenu record);

    Long insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectMenuList(SysMenu menu);
    List<SysMenu> selectMenuListByUserId(SysMenu menu);
    List<String> selectPermsByUserId(Long userId);
    List<SysMenu> selectMenuNormalAll();

    int selectCountMenuByParentId(Long parentId);


    /**
     * 查询系统所有菜单（含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuAll();

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuAllByUserId(Long userId);

    /**
     * 根据角色ID查询菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    public List<String> selectMenuTree(Long roleId);

    List<SysMenu> selectMenusByUserId(Long userId);
}