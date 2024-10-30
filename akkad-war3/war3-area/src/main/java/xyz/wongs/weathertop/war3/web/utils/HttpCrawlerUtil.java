package xyz.wongs.weathertop.war3.web.utils;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.conn.ConnectTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.wongs.weathertop.war3.web.zonecode.exception.NoIpAddressException;

import java.io.IOException;

/**
 * @author WCNGS@QQ.CO
 * @version V1.0
 * @Title:
 * @Package spring-cloud xyz.wongs.tools.utils
 * @Description: TODO
 * @date 2018/7/3 14:39
 **/
public class HttpCrawlerUtil {

    public static String CONTENT_TYPE_JSON = "application/json";
    public static int timeOut = 20000;


    public static Document getDocument(String url) throws IOException, NoIpAddressException {
        String ipport = "194.67.201.106:3128,194.88.105.156:3128,110.164.181.164:8081,180.183.235.3:9999,149.56.103.184:10000,218.60.8.98:3129";
        Document doc;
        WebClient client = null;
        try {
            proxyIp(ipport);
            client = getWebClient(ipport);
            doc = getStrByWebHtml(url,client);
        } finally {
            if(null!=client){
                client.close();
            }
        }
        return  doc;
    }

    public static void proxyIp(String ipport) throws NoIpAddressException{
        if(ipport == null) {
             throw new NoIpAddressException(" Proxy Ip is null");
        }
    }

    /**
     * 方法实现说明
     * @method      getWebClient
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param ipport  ipport格式类型为{IP:PORT}
     * @return      com.gargoylesoftware.htmlunit.WebClient
     * @exception
     * @date        2018/7/3 15:18
     */
    public static WebClient getWebClient(String ipport){

        String headers =  "https://mdskip.taobao.com/core/initItemDetail.htm?itemId=524456065159&isSecKill=false&spm=2014.1.52.78";
        BrowserVersion[] versions = {BrowserVersion.BEST_SUPPORTED, BrowserVersion.CHROME, BrowserVersion.EDGE, BrowserVersion.FIREFOX_45};
        WebClient client = new WebClient(versions[(int) (versions.length * Math.random())]);

        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setTimeout(timeOut);
        client.getOptions().setAppletEnabled(true);
        client.getOptions().setGeolocationEnabled(true);
        client.getOptions().setRedirectEnabled(true);
        client.addRequestHeader("Referer",headers );
        proAddress(ipport,client);

        return client;
    }



    /**
     * 根据IP列表设置代理ip地址
     * @method      proxyIpAddress
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param ipport
     * @param client
     * @return      WebClient
     * @exception
     * @date        2018/7/2 23:06
     */
    public static void proAddress(String ipport,WebClient client){
        ProxyConfig proxyConfig = new ProxyConfig((ipport.split(",")[0]).split(":")[0], Integer.parseInt((ipport.split(",")[0]).split(":")[1]));
        client.getOptions().setProxyConfig(proxyConfig);
    }



    public static Document getStrByWebHtml(String url, WebClient client) throws ConnectTimeoutException,IOException {
        Page page = client.getPage(url);
        WebResponse response = page.getWebResponse();

        /**
         *JSON直接格式化为字符串
         */
        String html = "";
        if (response.getContentType().equals(CONTENT_TYPE_JSON)) {
            html = response.getContentAsString();
        } else if (page.isHtmlPage()) {
            html = ((HtmlPage) page).asXml();
        }
        Document doc = Jsoup.parse(html);
        return doc;
    }
}
