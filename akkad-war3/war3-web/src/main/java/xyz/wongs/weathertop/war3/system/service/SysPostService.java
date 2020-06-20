package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.war3.system.entity.SysPost;
import xyz.wongs.weathertop.war3.system.mapper.SysPostMapper;

import java.util.List;


@Service
public class SysPostService extends BaseService<SysPost, Long> {

    @Autowired
    private SysPostMapper sysPostMapper;


    public List<SysPost> selectPostsByUserId(Long userId)
    {
        List<SysPost> userPosts = sysPostMapper.selectPostsByUserId(userId);
        List<SysPost> posts = sysPostMapper.selectPostAll();
        for (SysPost post : posts)
        {
            for (SysPost userRole : userPosts)
            {
                if (post.getId().longValue() == userRole.getId().longValue())
                {
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
    public List<SysPost> selectPostAll(){
        return sysPostMapper.selectPostAll();
    }

    @Override
    protected BaseMapper<SysPost, Long> getMapper() {
        return sysPostMapper;
    }
}