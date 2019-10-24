package xyz.wongs.weathertop.shiro.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Slf4j
public class ShiroFilterUtils {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/** 判断请求是否是ajax
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/10/23 15:30
	 * @param request
	 * @return boolean
	 * @throws
	 * @since
	 */
    public static boolean isAjax(ServletRequest request){
    	String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
    	if("XMLHttpRequest".equalsIgnoreCase(header)){
    		log.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,为Ajax请求");
    		return Boolean.TRUE;
    	}
		log.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,非Ajax请求");
    	return Boolean.FALSE;
    }

	/**
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/10/23 15:30
	 * @param response
	 * @param result
	 * @return void
	 * @throws
	 * @since
	 */
	public static void out(HttpServletResponse response, ResponseResult responseResult){

		PrintWriter out = null;
		try {
			//设置编码
			response.setCharacterEncoding("UTF-8");
			//设置返回类型
			response.setContentType("application/json");
			out = response.getWriter();
			//输出
			out.println(objectMapper.writeValueAsString(responseResult));
			log.error("用户在线数量限制【wyait-manage-->ShiroFilterUtils.out】响应json信息成功");
		} catch (Exception e) {
			log.error("用户在线数量限制【wyait-manage-->ShiroFilterUtils.out】响应json信息出错", e);
		}finally{
			if(null != out){
				out.flush();
				out.close();
			}
		}
	}

}
