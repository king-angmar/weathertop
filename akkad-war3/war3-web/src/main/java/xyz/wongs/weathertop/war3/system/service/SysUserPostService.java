package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.system.entity.SysUser;
import xyz.wongs.weathertop.war3.system.entity.SysUserPost;
import xyz.wongs.weathertop.war3.system.mapper.SysUserPostMapper;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysUserPostService extends BaseService<SysUserPost, Long> {

    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    @Override
    protected BaseMapper<SysUserPost, Long> getMapper() {
        return sysUserPostMapper;
    }


    @Transactional
    public void updateUserPost(SysUser user) {
        sysUserPostMapper.deleteUserPostByUserId(user.getId());
        insertUserPost(user);
    }
    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    @Transactional
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setId(super.getPrimaryKey(up));
                up.setUserId(user.getId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                sysUserPostMapper.batchUserPost(list);
            }
        }
    }
}