package xyz.wongs.weathertop.base.persistence.mybatis.config;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.PropertiesLoader;

import java.util.Map;

/**
 * 全局配置类 .在第一次调用的时候实例化自己
 * @author WCNGS@QQ.COM
 * @version 2014-06-25
 */
@Component
public class Global {

	private Global() {}
	/**
	 * 当前对象实例
	 */
	private static Global global = null;

	/**
	 * 静态工厂方法 获取当前对象实例 多线程安全单例模式(使用双重同步锁)
	 */
	public static synchronized Global getInstance() {
		if (global == null) {
			synchronized (Global.class) {
				if(global == null) {
					global = new Global();
				}
			}
		}
		return global;
	}

	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("config.properties");

	/**
	 * 获取配置
	 * @see
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
}
