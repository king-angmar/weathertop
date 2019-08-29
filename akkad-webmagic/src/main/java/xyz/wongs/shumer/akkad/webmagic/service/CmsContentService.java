package xyz.wongs.shumer.akkad.webmagic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.wongs.shumer.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.shumer.base.persistence.mybatis.service.BaseService;
import xyz.wongs.shumer.akkad.webmagic.entity.CmsContent;
import xyz.wongs.shumer.akkad.webmagic.mapper.CmsContentMapper;

@Slf4j
@Transactional
@Service
public class CmsContentService extends BaseService<CmsContent, Long> {

    @Autowired
    private CmsContentMapper cmsContentMapper;

    @Override
    protected BaseMapper<CmsContent, Long> getMapper() {
        return cmsContentMapper;
    }
}
