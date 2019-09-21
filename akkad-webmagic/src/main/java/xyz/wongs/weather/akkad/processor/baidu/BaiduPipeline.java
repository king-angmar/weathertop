package xyz.wongs.weather.akkad.processor.baidu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import xyz.wongs.weather.akkad.webmagic.entity.CmsContent;
import xyz.wongs.weather.akkad.webmagic.service.CmsContentService;

import java.util.Date;

@Component
public class BaiduPipeline implements Pipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduPipeline.class);

    @Autowired
    private CmsContentService cmsContentService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String answer = resultItems.get("answer");

        CmsContent cmsContent = new CmsContent();
        cmsContent.setTitle(title);
        cmsContent.setReleaseDate(new Date());
        cmsContent.setContent(answer);
        try {
            boolean success = cmsContentService.insert(cmsContent)>0;
            LOGGER.info("保存知乎文章成功：{}", title);
        } catch (Exception ex) {
            LOGGER.error("保存知乎文章失败", ex);
        }
    }
}
