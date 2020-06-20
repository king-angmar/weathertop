package xyz.wongs.weathertop.war3.web.zonecode.task.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.wongs.weathertop.domain.location.entity.Location;
import xyz.wongs.weathertop.domain.location.service.LocationService;
import xyz.wongs.weathertop.war3.web.utils.IdClazzUtils;
import xyz.wongs.weathertop.war3.web.utils.ZoneCodeStringUtils;
import xyz.wongs.weathertop.war3.web.zonecode.task.ProcessService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @ClassName:  JsoupProcessServiceImpl
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: <a href="wcngs@qq.com">WCNGS</a>
 * @date:   2017年7月28日 上午11:31:30  *
 * @Copyright: 2017 WCNGS Inc. All rights reserved.
 */
@Slf4j
@Service("processService")
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	@Qualifier("locationService")
	LocationService locationService;

	@Autowired
	IdClazzUtils idClazzUtils;


	@Override
	public void getHTML(String url, Location location) {
		List<Location> firstLevelLocas = getLocationOneLevel(url,location.getLocalCode());
		save(firstLevelLocas);
	}

	@Override
	public boolean getHTML2(String url,Location location) {
		try {
			List<Location> secondLevelLocas =getLocationSecondLevel(url,location);
			save(secondLevelLocas);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 初始化省、直辖区、自治区
	 * @method      intiRootUrl
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param url
	 * @return      void
	 * @exception
	 * @date        2018/6/30 23:29
	 */
	@Override
	public boolean intiRootUrl(String url){
		try {
			List<Location> rootLocations = getLocationRoot(url,"0");
			save(rootLocations);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Location> getLocationRoot(String url,String pCode){

		List<Location> locas = new ArrayList<Location>(27);
		Elements eles = getElementsByConnection(url,"provincetr");
		for(Element e:eles){
			Elements el2 = e.getElementsByTag("td").select("a[href]");
			if(null==el2 || el2.size()==0){
				continue;
			}
			for(Element et:el2) {
				String urls = et.attributes().asList().get(0).getValue();
				locas.add(new Location(urls.substring(0, 2), et.text(), "0", urls, 0));
			}
		}
		return locas;
	}

	/**
	 * 方法实现说明
	 * @method      thridLevelResolve
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param url
	 * @param location
	 * @return      void
	 * @exception
	 * @date        2018/7/1 9:50
	 */
	@Override
	public void thridLevelResolve(String url,Location location){
		this.thridLevelResolve(url,location,"Y");
	}


	/**
	 * 方法实现说明
	 * @method      thridLevelResolve
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param url
	 * @param location
	 * @param flag
	 * @return      void
	 * @exception
	 * @date        2018/7/1 16:24
	 */
	@Override
	public void thridLevelResolve(String url,Location location,String flag){

		try {
			if(StringUtils.isEmpty(location.getUrl())){
				return;
			}
			List<Location> thridLevelLocas =getLocation(url,new String[]{"towntr","href"},location.getLocalCode(),3,flag);
			location.setFlag(flag);
			locationService.updateByPrimaryKey(location);
			save(thridLevelLocas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(List<Location> locations){
		//结果为空，抛出异常
		if(null==locations || locations.isEmpty()){
			log.error(" target saved is null!");
			return ;
		}
		locationService.insertBatchByOn(locations);

	}


	public  void getLocationFourthLevel(String url,List<Location> thridLevelLocas){
		String prefix = ZoneCodeStringUtils.interceptionStringByLastIndexOf(url,"/")+"/";
		List<Location> locations = new ArrayList<Location>(20);
		for(Location le:thridLevelLocas){
			Elements es = getElementsByConnection(prefix+le.getUrl(),"villagetr");
			for(Element e:es){
				locations.add(new Location(e.child(0).text(),e.child(2).text(),le.getLocalCode(),null,4));
			}
		}

		locationService.insertBatchByOn(locations);


	}


	/**
	 *
	 * @Title: getLocationSecondLevel
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param url
	 * @param location
	 * @return
	 * @return: List<Location>
	 */
	public List<Location> getLocationSecondLevel(String url,Location location){
		List<Location> locas = null;
		try {
			locas = new ArrayList<Location>(90);
			//URL地址截取
			//标识位
			boolean flag =false;
			Elements es = getElementsByConnection(url,"countytr");
			if(null==es){
				log.error( url+ " 不能解析！" );
				return null;
			}
			Location tempLocation = null;
			for(Element e:es){
                //针对市辖区 这种无URL的做特殊处理
                if(!flag){
					tempLocation = new Location(e.child(0).text(),e.child(1).text(),location.getLocalCode(),null,2);
					tempLocation.setId(idClazzUtils.getId(Location.class));
                    locas.add(tempLocation);
                    //标识位置为TURE
                    flag=true;
                    continue;
                }
				es = e.getElementsByAttribute("href");
				if(es.size()==0){
					tempLocation = new Location(e.child(0).text(),e.child(1).text(),location.getLocalCode(),"",2);
					tempLocation.setId(idClazzUtils.getId(Location.class));
					locas.add(tempLocation);
					continue;
				}
				List<Attribute> attrs = es.get(0).attributes().asList();
				tempLocation = new Location(es.get(0).text(),es.get(1).text(),location.getLocalCode(),attrs.get(0).getValue(),2);
				tempLocation.setId(idClazzUtils.getId(Location.class));
				locas.add(tempLocation);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locas;
	}



	/**
	 *
	 * @Title: getLocationOneLevel
	 * @Description:
	 * 1、获取第一级地市信息
	 * 2、第二级区县信息
	 * @param url
	 * @param pCode
	 * @return
	 * @return: List<Location>
	 */
	public List<Location> getLocationOneLevel(String url,String pCode){

		List<Location> locas = new ArrayList<Location>(20);
		Elements eles = getElementsByConnection(url,"citytr");
		if(null==eles){
			log.error( url+ " 不能解析！" );
			return null;
		}
		Location location = null;
		for(Element e:eles){
			eles = e.getElementsByAttribute("href");
			List<Attribute> attrs = eles.get(0).attributes().asList();
			location = new Location(eles.get(0).text(),eles.get(1).text(),pCode,attrs.get(0).getValue(),1);
			location.setId(idClazzUtils.getId(Location.class));
			locas.add(location);
		}
		return locas;
	}


	public List<Location> getLocation(String url,String[] cssClazz,String parentURLCode,Integer lv,String flag){
		List<Location> locas = new ArrayList<Location>(20);
		Elements eles = getElementsByConnection(url,cssClazz[0]);
		if(null==eles){
			log.error( url+ " 不能解析！" );
			return null;
		}
		Location location = null;
		for(Element e:eles){
			eles = e.getElementsByAttribute(cssClazz[1]);
			List<Attribute> attrs = eles.get(0).attributes().asList();
			location= new Location(eles.get(0).text(),eles.get(1).text(),parentURLCode,attrs.get(0).getValue(),lv,flag);
			location.setId(idClazzUtils.getId(Location.class));
			locas.add(location);
		}
		return locas;
	}
	/**
	 * 案例
	 * <tr class='towntr'>
	 * 	<td><a href='02/340102001.html'>340102001000</a></td>
	 * 	<td><a href='02/340102001.html'>明光路街道</a></td>
	 * </tr>
	 * @Title: getLocation
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param url
	 * @param cssClazz
	 * @param parentURLCode
	 * @return  List<Location>
	 */
	public List<Location> getLocation(String url,String[] cssClazz,String parentURLCode,Integer lv){
		 return getLocation(url,cssClazz,parentURLCode,lv);
	}


	/**
	 * 方法实现说明
	 * @method      getElementss
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param url
	 * @param clazzName
	 * @return      org.jsoup.select.Elements
	 * @exception
	 * @date        2018/7/2 11:28
	 */
	public Elements getElementsByConnection(String url,String clazzName){

		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");

			RequestConfig config = RequestConfig.custom()
					//.setProxy(proxy)
					//设置连接超时 ✔
					// 设置连接超时时间 10秒钟
					.setConnectTimeout(10000)
					// 设置读取超时时间10秒钟
					.setSocketTimeout(10000)
					.build();
			httpget.setConfig(config);
			// 执行get请求
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			// 获取返回实体
			String content = EntityUtils.toString(entity, "GBK");
			// ============================= 【Jsoup】 ====================================
			//获取响应类型、内容
//			logger.error("Status:"+response.getStatusLine().getStatusCode());
//			logger.error("Content-Type:"+entity.getContentType().getValue());

//			Connection connection = HttpConnection.connect(new URL(url)).timeout(15000);
//			Document doc = connection.get();
			Document doc = Jsoup.parse(content);
			return doc.getElementsByClass(clazzName);
		} catch (ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			log.error(" URL: "+url+",clazzName:"+clazzName);
		} catch (IOException e) {
			log.error(" URL: "+url+",clazzName:"+clazzName);
		}
		return null;
	}

	@Override
	public void getLocationThrid() {
		String prefix ="http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/";
		List<Location> ll = locationService.findLocationThrid(3,31);
		for(Location le:ll){
			List<Location> llv2 = locationService.findLocationTreeNodeByLocalCode(le.getLocalCode());
			String url= prefix+appengUrl(llv2);

			Elements eles = getElementsByConnection(url,"villagetr");
			List<Location> locas = new ArrayList<Location>(eles.size());
			for(Element e:eles){
				locas.add(new Location(e.child(0).text(),e.child(2).text(),le.getLocalCode(),null,4));
//				System.out.println("toString() => " + new Location(e.child(0).text(),e.child(2).text(),le.getLocalCode(),null,4));
			}
			locationService.updateLocationFlag("3",le.getId());
			save(locas);
			try {
				Thread.sleep(new Random().nextInt(10)*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	public String appengUrl(List<Location> locations){
		Iterator<Location> it =  locations.iterator();
		String url = "";
		while(it.hasNext()){
			Location ln =it.next();
			String str= ln.getUrl();
			if (ln.getLv()==3){
				url=str;
			} else{
				int i = ln.getUrl().indexOf("/");
				url = str.substring(0, i)+"/"+url;
			}
		}
		return url;
	}

}
