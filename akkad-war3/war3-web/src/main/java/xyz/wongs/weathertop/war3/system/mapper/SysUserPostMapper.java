package xyz.wongs.weathertop.war3.system.mapper;

import xyz.wongs.weathertop.base.persistence.mybatis.annotation.MyBatisMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.war3.system.entity.SysUserPost;
import xyz.wongs.weathertop.war3.system.entity.SysUserRole;

import java.util.List;

@MyBatisMapper
public interface SysUserPostMapper extends BaseMapper<SysUserPost,Long> {
    int deleteByPrimaryKey(Long userPostId);

    Long insert(SysUserPost record);

    Long insertSelective(SysUserPost record);

    SysUserPost selectByPrimaryKey(Long userPostId);

    int updateByPrimaryKeySelective(SysUserPost record);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    int batchUserPost(List<SysUserPost> sysUserPostList);


    int updateByPrimaryKey(SysUserPost record);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int countUserPostById(Long postId);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    int deleteUserPostByUserId(Long userId);
}