package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.exception.user.BusinessException;
import xyz.wongs.weathertop.war3.system.entity.SysPost;
import xyz.wongs.weathertop.war3.system.mapper.SysPostMapper;
import xyz.wongs.weathertop.war3.system.mapper.SysUserPostMapper;

import java.util.List;


@Service
public class SysPostService extends BaseService<SysPost, Long> {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostCodeUnique(SysPost post)
    {
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = sysPostMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != postId.longValue())
        {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getId()) ? -1L : post.getId();
        SysPost info = sysPostMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != postId.longValue()) {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    public SysPost selectPostById(Long postId) {
        return sysPostMapper.selectByPrimaryKey(postId);
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(Long postId) {
        return sysUserPostMapper.countUserPostById(postId);
    }

    /**
     * 批量删除岗位信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    public int deletePostByIds(String ids) throws BusinessException {
        Long[] postIds = Convert.toLongArray(ids);
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return sysPostMapper.deletePostByIds(postIds);
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    public List<SysPost> selectPostList(SysPost post) {
        return sysPostMapper.selectPostList(post);
    }

    public List<SysPost> selectPostsByUserId(Long userId) {
        List<SysPost> userPosts = sysPostMapper.selectPostsByUserId(userId);
        List<SysPost> posts = sysPostMapper.selectPostAll();
        for (SysPost post : posts) {
            for (SysPost userRole : userPosts) {
                if (post.getId().longValue() == userRole.getId().longValue()) {
                    post.setFlag(true);
                    break;
                }
            }
        }
        return posts;
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    public List<SysPost> selectPostAll() {
        return sysPostMapper.selectPostAll();
    }

    @Override
    protected BaseMapper<SysPost, Long> getMapper() {
        return sysPostMapper;
    }
}