package xyz.wongs.weathertop.handball.task;


import xyz.wongs.weathertop.handball.location.entity.Location;


public interface ProcessService {


	boolean getHTML2(String url, Location location);
	/**
	 *
	 * @Title: htmlParser
	 * @Description: 解析HTML
	 * @return: void
	 */
	void getHTML(String url, Location location);

	void thridLevelResolve(String url, Location location);
	void thridLevelResolve(String url, Location location, String flag);


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
	boolean intiRootUrl(String url);



}
