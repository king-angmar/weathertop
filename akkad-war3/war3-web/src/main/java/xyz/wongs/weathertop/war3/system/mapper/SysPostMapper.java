package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysPost;

import java.util.List;

@MyBatisMapper
public interface SysPostMapper extends BaseMapper<SysPost,Long> {
    int deleteByPrimaryKey(Long postId);

    Long insert(SysPost record);

    Long insertSelective(SysPost record);

    SysPost selectByPrimaryKey(Long postId);

    int updateByPrimaryKeySelective(SysPost record);

    /**
     * 校验岗位名称
     *
     * @param postName 岗位名称
     * @return 结果
     */
    SysPost checkPostNameUnique(String postName);

    /**
     * 校验岗位编码
     *
     * @param postCode 岗位编码
     * @return 结果
     */
    SysPost checkPostCodeUnique(String postCode);

    /**
     * 批量删除岗位信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deletePostByIds(Long[] ids);

    /**
     * 查询岗位数据集合
     *
     * @param post 岗位信息
     * @return 岗位数据集合
     */
    List<SysPost> selectPostList(SysPost post);

    int updateByPrimaryKey(SysPost record);
    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    public List<SysPost> selectPostsByUserId(Long userId);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    public List<SysPost> selectPostAll();
}