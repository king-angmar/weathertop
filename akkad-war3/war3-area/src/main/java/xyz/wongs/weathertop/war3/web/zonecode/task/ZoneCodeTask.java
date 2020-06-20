package xyz.wongs.weathertop.war3.web.zonecode.task;

import org.apache.http.conn.ConnectTimeoutException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.domain.location.entity.Location;
import xyz.wongs.weathertop.war3.web.utils.HttpCrawlerUtil;
import xyz.wongs.weathertop.war3.web.zonecode.exception.NoIpAddressException;

import java.io.IOException;
import java.util.*;

/**
 * @author WCNGS@QQ.CO
 * @version V1.0
 * @Title:
 * @Package spring-cloud xyz.wongs.tools.dynamicIp
 * @Description: TODO
 * @date 2018/7/2 14:45
 **/
@Component
public class ZoneCodeTask {

    public static String RESULT_KEY_FLAG = "flag";
    public static String RESULT_KEY = "location";


    public static void main(String[] args) throws Exception {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/";

        Map<String, Object> result = new ZoneCodeTask().parseHtmlFun(url,0);
        Object obj = result.get(RESULT_KEY_FLAG);
        if(null== obj) {
            List<Location> locations = (List<Location>)result.get(RESULT_KEY);
            for (Location location:locations){
                System.out.println(location.toString());
            }
        } else {
            //根据异常情况 在做处理，例如链接超时类，就要不断换IP.....待补充
        }
    }




    /**
     *
     * @method      parseHtmlFun
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param url
     * @return      java.util.Map<java.lang.String,java.lang.Object>
     *     仅当RESULT_KEY_FLAG值不为空得时候，根据int类型的值，判断如下
     *      -1:连接超时；大于3得则代理IP为空；否则为IO异常
     * @exception
     * @date        2018/7/2 22:33
     */
    public Map<String, Object> parseHtmlFun(String url,int level) {
        Map<String, Object> map = new HashMap<String, Object>(2);
        try {

            Document doc = HttpCrawlerUtil.getDocument(url);
            List<Location> locations;
            switch (level){
                //第一级：省、直辖市初始化
                case 0:
                    locations = rootLevel(doc);
                    break;
                case 1:

                    break;
                default: break;
            }
            locations = rootLevel(doc);

            map.put(RESULT_KEY,locations);
        } catch (NoIpAddressException e) {
            map.put(RESULT_KEY_FLAG, new Random().nextInt(20)*10);
        } catch (ConnectTimeoutException e) {
            map.put(RESULT_KEY_FLAG, -1);
        } catch (IOException e) {
            map.put(RESULT_KEY_FLAG, new Random().nextInt(3));
        }
        return map;
    }

    public List<Location> rootLevel(Document doc){
        List<Location> locations = new ArrayList<Location>(35);
        Elements els = doc.getElementsByClass("provincetr");
        for (Element element : els) {
            Elements es = element.select("a[href]");
            if(null==es || es.size()==0){
                continue;
            }
            int m = es.size();
            for (int j = 0; j < m; j++) {
                String prl = es.get(j).attributes().asList().get(0).getValue();
                String localCode = prl.substring(0, 2);
                String localName = es.get(j).text();
                Location location = new Location(localCode,localName,"0",prl,0);
                locations.add(location);
            }
        }
       return locations;
    }

}
