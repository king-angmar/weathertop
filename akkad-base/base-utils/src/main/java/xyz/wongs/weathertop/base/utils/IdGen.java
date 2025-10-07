/**
 * Copyright &copy; 2012-2016 <a href="https://wongs.xyz">UECC</a> All rights reserved.
 */
package xyz.wongs.weathertop.base.utils;

import java.security.SecureRandom;
import java.util.UUID;

//import org.activiti.engine.impl.cfg.IdGenerator;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *  ┏┓　　　┏┓
 *┏┛┻━━━┛┻┓
 *┃　　　　　　　┃ 　
 *┃　　　━　　　┃
 *┃　┳┛　┗┳　┃
 *┃　　　　　　　┃
 *┃　　　┻　　　┃
 *┃　　　　　　　┃
 *┗━┓　　　┏━┛
 *　　┃　　　┃神兽保佑
 *　　┃　　　┃代码无BUG！
 *　　┃　　　┗━━━┓
 *　　┃　　　　　　　┣┓
 *　　┃　　　　　　　┏┛
 *　　┗┓┓┏━┳┓┏┛
 *　　　┃┫┫　┃┫┫
 *　　　┗┻┛　┗┻┛
 * @ClassName IdGen
 * @Description 封装各种生成唯一性ID算法的工具类.
 * @author WCNGS@QQ.COM
 * @date 2019/2/27 18:08
 * @Version 1.0.0
*/
@Service
@Lazy(false)
public class IdGen {
//	public class IdGen implements IdGenerator, SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
//	/**
//	 * Activiti ID 生成
//	 */
//	@Override
//	public String getNextId() {
//		return IdGen.uuid();
//	}
//
//	@Override
//	public Serializable generateId(Session session) {
//		return IdGen.uuid();
//	}
	
//	public static void main(String[] args) {
//		System.out.println(IdGen.uuid());
//		System.out.println(IdGen.uuid().length());
//
//	}

}
