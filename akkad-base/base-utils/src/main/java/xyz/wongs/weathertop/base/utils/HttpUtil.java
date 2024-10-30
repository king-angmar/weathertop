package xyz.wongs.weathertop.base.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * HTTP协议操作类
 *
 * @author zfz
 */
public class HttpUtil {
  private static Log log = LogFactory.getLog(HttpUtil.class);

  /**
   * 往目的地址post xml信息
   *
   * @param postUrl
   *            post目的url
   * @param xml
   *            xml内容
   * @return String
   * @throws Exception
   */
  public static String postXml(final String postUrl, final String xml,
                               final int timeOut) throws Exception {

    HttpUtil.log.info("req:" + xml);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(postUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.addRequestProperty("content-type", "text/xml;charset=gb2312");
    con.addRequestProperty("content-length", String.valueOf(xml.length()));
    con.setConnectTimeout(timeOut);//设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(8000);//设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    pw.print(xml);
    pw.close();
    final BufferedReader in = new BufferedReader(new InputStreamReader(con
            .getInputStream()));
    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());
    return rsp.toString();
  }

  /**
   * 往目的地址post xml信息
   * @param postUrl
   * @param xml
   * @param timeOut
   * @param reqCharset
   * @param rspCharset
   * @return
   * @throws Exception
   */
  public static String postXml(final String postUrl, final String xml,
                               final int timeOut, final String reqCharset, final String rspCharset) throws Exception {

    HttpUtil.log.info("req:" + xml);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(postUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.addRequestProperty("content-type", "text/xml;charset="+reqCharset);
    con.addRequestProperty("content-length", String.valueOf(xml.length()));
    //设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setConnectTimeout(timeOut);
    //设置从主机读取数据超时长（毫秒） 两时间参数要同时设置 （Update By ChenW 2019-01-04 与计费联调发现个别接口读取时间很长）
    if( 0 == timeOut) {
      con.setReadTimeout(600000);
    } else {
      con.setReadTimeout(timeOut);
    }
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    pw.print(xml);
    pw.close();

    BufferedReader in = null;
    if(!"".equals(rspCharset)){
      in = new BufferedReader(new InputStreamReader(con.getInputStream(),rspCharset));
    } else {
      in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());
    return rsp.toString();
  }

  /**
   * 使用HTTPClient的方式
   * @param paramter
   * @param timeOut
   * @param url
   * @return
   * @throws Exception
   * note by xingzh 2017-06-08 crm00096341 宽带认证系统接口地址变更
   * 		该方法仅用于宽带认证系统接口，通过httpClient的方式调用门户部署在202.101.128.200上的工程
   * 		最终调用宽带认证系统开放在外网的webservice服务，该接口使用的客户端类为bdPwdChangeClient
   */
//  public static String respByHttpClient(String paramter,int timeOut,String  url,String methodName) throws Exception{
//
//    String responseMessage="";
//    final HttpClient httpClient = new HttpClient();
//    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
//            timeOut);
//    httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
//    PostMethod postMethod = new PostMethod(url);
//    // 填入各个表单域的值
//    final NameValuePair[] datas = new NameValuePair[3];
//    final NameValuePair methodPair = new NameValuePair("methodName",methodName);
//    final NameValuePair xmlPair = new NameValuePair("reqParams",
//            paramter);
//    final NameValuePair clientNamepair = new NameValuePair("clientName",
//            "bdPwdChangeClient");
//
//    datas[0] = methodPair;
//    datas[1] = xmlPair;
//    datas[2] = clientNamepair;
//    // 将表单的值放入postMethod中
//    postMethod.setRequestBody(datas);
//
//    int  statusCode = httpClient.executeMethod(postMethod);
//    if (statusCode == HttpStatus.SC_OK) {
//      responseMessage =new String(CryptTool.decrypt(CryptTool.base64DecodeToBytes(new String(postMethod.getResponseBody()))));
//    } else {
//      log.error("调用外网接口代理服务错误，返回码:"+statusCode);
//    }
//
//    return responseMessage;
//  }


  /**
   * 使用HTTPClient的方式
   * @param paramter
   * @param timeOut
   * @param url
   * @return
   * @throws Exception
   */
  public static String getRespByHttpClient(String paramter,int timeOut,String  url,String methodName,String clientName) throws Exception{

    String responseMessage="";
    final HttpClient httpClient = new HttpClient();
    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
            timeOut);
    httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
    PostMethod postMethod = new PostMethod(url);
    // 填入各个表单域的值
    final NameValuePair[] datas = new NameValuePair[3];
    final NameValuePair methodPair = new NameValuePair("methodName",methodName);
    final NameValuePair xmlPair = new NameValuePair("reqParams",
            paramter);
    final NameValuePair clientNamepair = new NameValuePair("clientName",
            clientName);

    datas[0] = methodPair;
    datas[1] = xmlPair;
    datas[2] = clientNamepair;
    // 将表单的值放入postMethod中
    postMethod.setRequestBody(datas);

    int  statusCode = httpClient.executeMethod(postMethod);
    if (statusCode == HttpStatus.SC_OK) {
      responseMessage = new String(postMethod.getResponseBody());
    } else {
      log.error("调用外网接口代理服务错误，返回码:"+statusCode);
    }

    return responseMessage;
  }

  /**
   * 与PostPage类似，只是增加了GBK字符集处理，支持汉字
   * @param pageUrl
   * @param content
   * @return
   */
  public static String PostPage1(String pageUrl, String content,int timeOut) {
    String res = "";
    try {
      URL url;
      HttpURLConnection urlConn;
      DataOutputStream printout;
      BufferedReader input;
      url = new URL(pageUrl);
      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setConnectTimeout(timeOut);// 连接主机的超时时间（单位：毫秒）
      urlConn.setReadTimeout(timeOut);//从主机读取数据的超时时间（单位：毫秒）
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      urlConn.setRequestMethod("POST");
      urlConn.setRequestProperty("Content-Type",
              "application/x-www-form-urlencoded");
      //发送request
      urlConn.setRequestProperty("Content-Length", content.length() + "");
      printout = new DataOutputStream(urlConn.getOutputStream());
      byte[] pp=content.getBytes("GBK");
      printout.write(pp);
      printout.flush();
      printout.close();
      //取response
      input = new BufferedReader(new InputStreamReader(urlConn.
              getInputStream()));
      String str = "";
      int count;
      char[] chs = new char[1024];
      while ((count= input.read(chs)) != -1) {
        str += new String(chs,0,count);
        //char[] chs_temp=new char[1024];
        //chs=chs_temp;
      }
      res = str;
      input.close();
    } catch (Exception ex) {
      log.error("HttpUtil.PostPage(): url=" + pageUrl + ", content=" + content + ", ex=" + ex);
    }
    return res;
  }



  /**
   * Add By ChenW 2018-11-06
   * 与PostPage类似，只是增加了GBK字符集处理，支持汉字
   * @param pageUrl
   * @param content
   * @return
   */
  public static String postAboutGBK(String pageUrl, String content,int timeOut) {
    String res = "";
    try {
      URL url;
      HttpURLConnection urlConn;
      DataOutputStream printout;
      BufferedReader input;
      url = new URL(pageUrl);
      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setConnectTimeout(timeOut);// 连接主机的超时时间（单位：毫秒）
      urlConn.setReadTimeout(timeOut);//从主机读取数据的超时时间（单位：毫秒）
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      urlConn.setRequestMethod("POST");
      urlConn.setRequestProperty("Content-Type",
              "application/x-www-form-urlencoded");
      /**
       * content-length这个是所传报文的byte类型的长度，而并非string字符串的长度，tomcat的编码格式iso-8859-1 ，
       * 而weblogic是GBK（或UTF-8）  这样就导致了超长。
       * 重点是本地用Tomcat 运行正常，而Weblogic运行就不正常了。所以只能这样做了。
       */
      urlConn.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
      //发送request
//			urlConn.setRequestProperty("Content-Length", content.length() + "");

      printout = new DataOutputStream(urlConn.getOutputStream());
      byte[] pp=content.getBytes("GBK");
      printout.write(pp);
      printout.flush();
      printout.close();
      //取response
      input = new BufferedReader(new InputStreamReader(urlConn.
              getInputStream(),"GBK"));
      String str = "";
      int count;
      char[] chs = new char[1024];
      while ((count= input.read(chs)) != -1) {
        str += new String(chs,0,count);
      }
      res = str;
      input.close();
    } catch (Exception ex) {
      log.error("HttpUtil.PostPage(): url=" + pageUrl + ", content=" + content + ", ex=" + ex);
    }
    return res;
  }

  /**
   * httpClientGet方式请求
   * @param url
   * @param timeOut
   * @param encoding
   * @return
   * @throws Exception
   */
  public static String httpClientGet(String url,int timeOut,String encoding) throws Exception{
    String responseMessage="";
    final HttpClient httpClient = new HttpClient();
    httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
            timeOut);
    httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
    GetMethod getMethod = new GetMethod(url);
    int  statusCode = httpClient.executeMethod(getMethod);
    if (statusCode == HttpStatus.SC_OK) {
      responseMessage = new String(getMethod.getResponseBody(),encoding);
    } else {
      log.error("调用外网接口代理服务错误，返回码:"+statusCode);
    }
    return responseMessage;
  }

  /**
   * 远程地址调用
   *
   * @param:
   * @exception:
   * @return: String
   */
  public static String HttpPost(final String pageUrl, final String content,
                                final int timeout, final int autoAddTi) throws Exception {
    String res = "";
    try {
      URL url;
      HttpURLConnection urlConn;
      DataOutputStream printout;
      BufferedReader input;
      // 增加随机数，避免缓存
      final long ct = System.currentTimeMillis();
      final Random random = new Random(ct);
      String npageUrl = pageUrl + "";
      if (autoAddTi == 1) {
        if (npageUrl.indexOf("?") > -1) {
          npageUrl += "&ti=" + ct + random.nextInt();
        } else {
          npageUrl += "?ti=" + ct + random.nextInt();
        }
      } else if (autoAddTi == 2) {
        npageUrl += "?ti=" + ct + random.nextInt();
      } else if (autoAddTi == 3) {
        npageUrl += "&ti=" + ct + random.nextInt();
      }
      url = new URL(npageUrl);
      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      urlConn.setRequestMethod("POST");
      urlConn.setRequestProperty("Content-Type",
              "application/x-www-form-urlencoded");
      // 设置超时
      int realTimeOut = 0;
      String timeOut = null;
      try {
        //      SysConfigService sysConfigService = (SysConfigService) ApplicationContextUtil.getBean("sysConfigServiceImpl");
        //     timeOut = sysConfigService.getSysConfigValue("PUB_HTTP_POST_REQ_TIME_OUT");

      } catch (Throwable t) {
        timeOut = "5000";
      }
//			if ("".equals(timeOut)) {
//				realTimeOut = timeout;
//			} else {
//				realTimeOut = Integer.parseInt(timeOut);
//			}

      if(timeout!=0){
        realTimeOut = timeout;
      }else{
        realTimeOut = Integer.parseInt(timeOut);
      }
      urlConn.setConnectTimeout(realTimeOut);// 设置连接主机超时
      urlConn.setReadTimeout(realTimeOut);// 设置从主机读取数据超时

      // 发送request
      final byte[] pp = content.getBytes("GBK");
      urlConn.setRequestProperty("Content-Length", pp.length + "");
      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      // 取response
      input = new BufferedReader(new InputStreamReader(urlConn
              .getInputStream(), "GBK"));
      final StringBuffer str = new StringBuffer();
      String readLine = input.readLine();
      while (readLine != null) {
        str.append(readLine);
        readLine = input.readLine();
      }
      // int count;
      // final char[] chs = new char[1024000];
      // while ((count = input.read(chs)) != -1) {
      // // str += new String(chs);
      // str.append(chs);
      // }
      res = str.toString().trim();
      input.close();
    } catch (final Exception ex) {
      throw ex;
    }
    return res;
  }

  /**
   * 参数为JSON格式的POST请求方式
   * @param url
   * @param param
   * @param timeout
   * @return String
   * 		add by xukc 2018-01-25 crm00107262：随销专区--关联信息区
   */
  public static String sendPost(String url, String param, int timeout) throws Exception {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection conn = realUrl.openConnection();

      // 设置通用的请求属性
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("user-agent",
              "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      conn.setConnectTimeout(timeout);//设置连接主机超时长（毫秒）两时间参数要同时设置
      conn.setReadTimeout(timeout);//设置从主机读取数据超时长（毫秒） 两时间参数要同时设置

      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());

      // 发送请求参数
      out.print(param);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(
              new InputStreamReader(conn.getInputStream(),"utf-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      throw e;
    }
    //使用finally块来关闭输出流、输入流
    finally{
      try{
        if(out!=null){
          out.close();
        }
        if(in!=null){
          in.close();
        }
      }
      catch(IOException ex){
        throw ex;
      }
    }
    return result;
  }


  /**
   * GET请求方式
   * @param getUrl
   * @param timeOut
   * @param reqCharset
   * @param rspCharset
   * @param keyMap
   * @return
   * @throws Exception
   * 		add by xukc 2018-08-08
   */
  public static String httpGet(final String getUrl, final int timeOut,
                               final String reqCharset, final String rspCharset,
                               Map<String, String> keyMap) throws Exception {
    HttpUtil.log.info("req:" + getUrl);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(getUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.setRequestProperty("Content-Encoding", reqCharset);
    con.setRequestProperty("X-APP-ID", keyMap.get("appId"));
    con.setRequestProperty("X-APP-KEY", keyMap.get("appKey"));
//		con.setRequestProperty("X-CTG-Request-ID","");
//		con.setRequestProperty("X-CTG-Region-ID","");
    con.setConnectTimeout(timeOut);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(8000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod("GET");
    BufferedReader in = null;
    int code = con.getResponseCode();
    if (!"".equals(rspCharset)) {
      if (200 == code) {
        in = new BufferedReader(new InputStreamReader(
                con.getInputStream(), rspCharset));
      } else {
        in = new BufferedReader(new InputStreamReader(
                con.getErrorStream(), rspCharset));
      }
    } else {
      if (200 == code) {
        in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
      } else {
        in = new BufferedReader(new InputStreamReader(
                con.getErrorStream()));
      }
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());
    return rsp.toString();
  }

  /**
   * POST请求方式
   * @param postUrl
   * @param json
   * @param timeOut
   * @param keyMap
   * @return
   * @throws Exception
   * 		add by xukc 2018-08-08
   */
  public static String httpPost(final String postUrl, final String json,
                                final int timeOut, Map<String, String> keyMap) throws Exception {
    HttpUtil.log.info("req:" + json);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(postUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.addRequestProperty("Content-Type", "application/json;charset=utf-8");
    con.addRequestProperty("content-length", String.valueOf(json.length()));
    con.setRequestProperty("X-APP-ID", keyMap.get("appId"));
    con.setRequestProperty("X-APP-KEY", keyMap.get("appKey"));
    con.setConnectTimeout(timeOut);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(8000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    pw.print(json);

    pw.close();

    int code = con.getResponseCode();
    BufferedReader in = null;
    if (200 == code) {
      in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    } else {
      in = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());
    return rsp.toString();
  }

  /**
   * POST请求方式
   * @param postUrl
   * @param json
   * @param timeOut
   * @param keyMap
   * @return
   * @throws Exception
   * 		add by ChenW 2019-04-16
   * 	头部增加了一个X-CTG-REQUEST-ID字段
   */
  public static String httpPostAboutMore(final String postUrl, final String json,
                                         final int timeOut, Map<String, String> keyMap) throws Exception {
    HttpUtil.log.info("req:" + json);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(postUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.addRequestProperty("Content-Type", "application/json;charset=utf-8");
    con.addRequestProperty("content-length", String.valueOf(json.length()));
    con.setRequestProperty("X-APP-ID", keyMap.get("appId"));
    con.setRequestProperty("X-APP-KEY", keyMap.get("appKey"));
    con.setRequestProperty("X-CTG-Request-Id", keyMap.get("X_CTG_REQUEST_ID"));
    con.setConnectTimeout(timeOut);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(8000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    pw.print(json);

    pw.close();

    int code = con.getResponseCode();
    BufferedReader in = null;
    if (200 == code) {
      in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    } else {
      in = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());
    return rsp.toString();
  }

  /**
   * POST请求方式-中文参数
   * @param postUrl
   * @param json
   * @param timeOut
   * @param keyMap
   * @return
   * @throws Exception
   * 		add by zhangcy 2018-10-24
   */
  public static String httpPostFor(final String postUrl, final String json,
                                   final int timeOut, Map<String, String> keyMap,String encode) throws Exception {
    HttpUtil.log.info("req:" + json);
    final StringBuffer rsp = new StringBuffer();
    final URL url = new URL(postUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.addRequestProperty("Content-Type", "application/json;charset=utf-8");
    con.addRequestProperty("content-length", String.valueOf(json.length()));
    con.setRequestProperty("X-APP-ID", keyMap.get("appId"));
    con.setRequestProperty("X-APP-KEY", keyMap.get("appKey"));
    con.setConnectTimeout(timeOut);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(8000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod("POST");
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    DataOutputStream out = new DataOutputStream(con.getOutputStream());// 打开输出流往对端服务器写数据
    out.write(json.getBytes("UTF-8"));// 写数据
    out.flush();// 刷新
    out.close();// 关闭输出流
    BufferedReader reader = new BufferedReader(new InputStreamReader(
            con.getInputStream(), encode));// 往对端写完数据对端服务器返回数据
    // ,以BufferedReader流来读取
    StringBuffer buffer = new StringBuffer();
    String line = "";
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    reader.close();
    return buffer.toString();

  }


  /**
   * POST请求方式
   * @param postUrl
   * @param json
   * @param timeOut
   * @param keyMap
   * @return
   * @throws Exception
   * 		add by xukc 2018-08-08
   */
  public static Map httpPost2(final String postUrl, final String json,
                              final int timeOut, Map<String, String> keyMap,String method) throws Exception {
    HttpUtil.log.info("req:" + json);
    final StringBuffer rsp = new StringBuffer();
    //String jsonEn = URLEncoder.encode(json, "UTF-8");
    //final URL url = new URL(postUrl+"/"+jsonEn);
    final URL url = new URL(postUrl);

    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.addRequestProperty("Content-Type", "application/json;charset=utf-8");
    con.addRequestProperty("content-length", String.valueOf(json.length()));
    if(keyMap!=null){
      con.setRequestProperty("X-APP-ID", StrUtil.strnull(keyMap.get("appId")));
      con.setRequestProperty("X-APP-KEY", StrUtil.strnull(keyMap.get("appKey")));
    }
    con.setConnectTimeout(15000);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(15000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod(method);
    con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);

    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    pw.print(json);

    pw.close();
    Map map = new HashMap();
    int code = con.getResponseCode();
    map.put("code",StrUtil.strnull(code));

    BufferedReader in = null;
    if (200 == code) {
      in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    } else {
      in = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());

    map.put("msg",rsp.toString());
    return map;
  }
  public static Map http(final String pacthUrl, final String json,
                         final int timeOut, Map<String, String> keyMap,String method) throws Exception {
    HttpUtil.log.info("req:" + json);
    final StringBuffer rsp = new StringBuffer();
    //String jsonEn = URLEncoder.encode(json, "UTF-8");
    final URL url = new URL(pacthUrl);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.addRequestProperty("Content-Type", "application/json;charset=utf-8");
    con.addRequestProperty("content-length", String.valueOf(json.length()));
    if(keyMap!=null){
      con.setRequestProperty("X-APP-ID", StrUtil.strnull(keyMap.get("appId")));
      con.setRequestProperty("X-APP-KEY", StrUtil.strnull(keyMap.get("appKey")));
    }
    con.setConnectTimeout(15000);// 设置连接主机超时长（毫秒）两时间参数要同时设置
    con.setReadTimeout(15000);// 设置从主机读取数据超时长（毫秒） 两时间参数要同时设置
    con.setRequestMethod(method);
    //con.setUseCaches(true);
    con.setDoInput(true);
    con.setDoOutput(true);
    final OutputStream os = con.getOutputStream();
    final PrintWriter pw = new PrintWriter(os);
    //写入json
    pw.print(json);

    pw.close();
    Map<String,String> map = new HashMap<String,String>();
    int code = con.getResponseCode();
    map.put("code",StrUtil.strnull(code));

    BufferedReader in = null;
    if (200 == code) {
      in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    } else {
      in = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
    }

    String line;
    while ((line = in.readLine()) != null) {
      rsp.append(line);
    }
    in.close();
    HttpUtil.log.info("rsp:" + rsp.toString());

    map.put("msg",rsp.toString());
    return map;
  }
}