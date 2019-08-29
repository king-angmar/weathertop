package xyz.wongs.shumer.akkad.processor.baidu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import xyz.wongs.shumer.akkad.processor.zhihu.ZhihuPageProcessor;
import xyz.wongs.shumer.akkad.processor.zhihu.ZhihuPipeline;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class BaiduTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhihuPipeline.class);

    @Autowired
    private BaiduPipeline baiduPipeline;

    @Autowired
    private BaiduProcessor baiduProcessor;

    private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    public void crawl() {
        // 定时任务，每10分钟爬取一次
        timer.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("BaiduCrawlerThread");

            try {
                Spider.create(baiduProcessor)
                        // 从https://www.zhihu.com/explore开始抓
                        .addUrl("http://news.baidu.com/")
                        // 抓取到的数据存数据库
                        .addPipeline(baiduPipeline)
                        // 开启2个线程抓取
                        .thread(2)
                        // 异步启动爬虫
                        .start();
            } catch (Exception ex) {
                LOGGER.error("定时抓取知乎数据线程执行异常", ex);
            }
        }, 0, 10, TimeUnit.MINUTES);
    }
}