package xyz.wongs.weathertop.base.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;


/**
 * @author 曾臻
 * @date 2012-10-20
 * 
 */

public class StrUtil {
	/**
	 * 号码段配置信息
	 */
	

	/**
	 * 如果是null转换成""
	 * 
	 * @author 曾臻
	 * @date 2012-10-24
	 * @param str
	 * @return
	 */
	public static String toString(String str) {
		if (str == null)
			return "";
		return str;
	}

	/**
	 * 判断字符是否为全数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	
	/**
	 * 判断是否为空串（null和""都是空串）
	 * 
	 * @author 曾臻
	 * @date 2012-11-20
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 解决下载文件名同时兼容IE和火狐而不出现乱码
	 */
	public static String encodeFileName(String fileName, String agent) {
		try {
			if (null != agent && -1 != agent.indexOf("MSIE")) {
				return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
				return "=?UTF-8?B?"
						+ (new String(Base64.encodeBase64(fileName
								.getBytes("UTF-8")))) + "?=";
			} else {
				return fileName;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 如果入参是null或者"",用另一入参rpt替代入参返回，否则返回入参的trim().
	 * 
	 * @param str
	 *            String
	 * @param rpt
	 *            String
	 * @return String
	 * @author: fanggq
	 * 
	 */
	public static String strnull(final String str, final String rpt) {
		if (str == null || str.equals("null") || str.equals("")
				|| str.trim() == null) {
			return rpt;
		} else {
			return str.trim();
		}
	}

	/**
	 * 为检查null值，如果为null，将返回""，不为空时将替换其非html符号.
	 * 
	 * @param strn
	 *            String
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final String strn) {
		return StrUtil.strnull(strn, "");
	}

	/**
	 * 为检查null值，如果为null，将返回""，不为空时将替换其非html符号.
	 * 
	 * @param str
	 *            Object
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final Object str) {
		String result = "";
		if (str == null) {
			result = "";
		} else {
			result = str.toString();
		}
		return result;
	}

	/**
	 * 适用于web层 为检查null值，如果为null，将返回"&nbsp;"，不为空时将替换其非html符号.
	 * 
	 * @param strn
	 *            String
	 * @return String
	 * @author: fanggq
	 */
	public static String repnull(final String strn) {
		return StrUtil.strnull(strn, "&nbsp;");
	}

	/**
	 * 把Date的转化为字符串，为空时将为"0000-00-00 00:00:00".
	 * 
	 * @param strn
	 *            Date
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final Date strn) {
		String str = "";

		if (strn == null) {
			str = "0000-00-00 00:00:00";
		} else {
			// strn.toGMTString();
			str = strn.toString();
		}
		return (str);
	}

	/**
	 * 把BigDecimal的转换为字符串，为空将返回0.
	 * 
	 * @param strn
	 *            BigDecimal
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final BigDecimal strn) {
		String str = "";

		if (strn == null) {
			str = "0";
		} else {
			str = strn.toString();
		}
		return (str);
	}

	/**
	 * 把int的转换为字符串(不为空，只起转换作用).
	 * 
	 * @param strn
	 *            int
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final int strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	/**
	 * 把float的转换为字符串(不为空，只起转换作用).
	 * 
	 * @param strn
	 *            float
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final float strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	/**
	 * 把long的转换为字符串(不为空，只起转换作用).
	 * 
	 * @param strn
	 *            float
	 * @return String
	 */
	public static String strnull(final long strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	/**
	 * 把double的转换为字符串(不为空，只起转换作用).
	 * 
	 * @param strn
	 *            double
	 * @return String
	 * @author: fanggq
	 */
	public static String strnull(final double strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	/**
	 * 将以sgn为分隔符的字符串转化为数组.
	 * 
	 * @param str
	 *            String
	 * @param sgn
	 *            String
	 * @return String[]
	 * @author: fanggq
	 */
	public static String[] split(String str, final String sgn) {
		String[] returnValue = null;
		if (!StrUtil.strnull(str).equals("")) {
			final Vector<String> vectors = new Vector<String>();
			int i = str.indexOf(sgn);
			String tempStr = "";
			for (; i >= 0; i = str.indexOf(sgn)) {
				tempStr = str.substring(0, i);
				str = str.substring(i + sgn.length());
				vectors.addElement(tempStr);
			}
			if (!str.equalsIgnoreCase("")) {
				vectors.addElement(str);
			}
			returnValue = new String[vectors.size()];
			for (i = 0; i < vectors.size(); i++) {
				returnValue[i] = (String) vectors.get(i);
				returnValue[i] = returnValue[i].trim();
			}
		}
		return returnValue;
	}

	/**
	 * 按长度分割字符串
	 * 
	 * @date 2013-2-2
	 * @param str
	 * @param len
	 * @return
	 */
	public static String[] split(String msg, int num) {
		int len = msg.length();
		if (len <= num)
			return new String[] { msg };

		int count = len / (num - 1);
		count += len > (num - 1) * count ? 1 : 0;

		String[] result = new String[count];

		int pos = 0;
		int splitLen = num - 1;
		for (int i = 0; i < count; i++) {
			if (i == count - 1)
				splitLen = len - pos;

			result[i] = msg.substring(pos, pos + splitLen);
			pos += splitLen;

		}

		return result;
	}

	/**
	 * 过滤掉换行
	 * 
	 * @date 2013-2-2
	 * @param htmlStr
	 * @return
	 */
	public static String filterRn(String htmlStr) {
		String textStr = "";
		Pattern p_rn;
		Matcher m_rn;
		try {
			String regEx_rn = "[\\r\\n]*";

			p_rn = Pattern.compile(regEx_rn, Pattern.CASE_INSENSITIVE);
			m_rn = p_rn.matcher(htmlStr);
			htmlStr = m_rn.replaceAll("");

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	/**
	 * 过滤HTML标签
	 * 
	 * @date 2013-2-2
	 * @param html
	 * @return
	 */
	public static String html2text(String htmlStr) {
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style>]*?>[\s\S]*?<\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	/*
	 * 产生两位的随机码
	 */

	public static String createRandomCode() {
		int random_int = (int) (Math.random() * 90 + 10);
		String random_string = Integer.toString(random_int);
		return random_string;
	}

	/**
	 * 
	 * @param len
	 *            需要显示的长度(<font color="red">注意：长度是以byte为单位的，一个汉字是2个byte</font>)
	 * @param symbol
	 *            用于表示省略的信息的字符，如“...”,“>>>”等。
	 * @return 返回处理后的字符串
	 * @throws UnsupportedEncodingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getLimitLengthString(String str, int len, String symbol) {
		int reInt = 0;
		String reStr = "";
		if (str == null)
			return "";
		try {
			if (str.getBytes("GBK").length <= len)
				return str;
			char[] tempChar = str.toCharArray();
			for (int kk = 0; (kk < tempChar.length && reInt < len
					- symbol.length()); kk++) {
				String s1 = String.valueOf(tempChar[kk]);
				byte[] b = s1.getBytes("GBK");
				reInt += b.length;
				reStr += tempChar[kk];
			}
			reStr += symbol;
		} catch (UnsupportedEncodingException e) {
		}
		return reStr;
	}

	/**
	 * 方法功能: 判断一个对象或者是字符串是否为空
	 * 
	 * @param
	 * @return
	 * @param str
	 * @return
	 * @author: Liuzhuangfei
	 * @修改记录： ==============================================================<br>
	 *        日期:2010-12-17 Liuzhuangfei 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static boolean isNullOrEmpty(final Object str) {
		boolean result = false;
		if (str == null || "null".equals(str)
				|| "".equals(str.toString().trim())) {
			result = true;
		}
		return result;
	}

	/**
	 * 生成全局标识码
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	public static void main(String[] args) {
		String aa = "需要显示的长度(<font";

		System.out.println(getLimitLengthString(aa, 20, "..."));
	}

	public static boolean isEmailStyle(String email) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	public static String getClazzName(Class<?> clazz) {
		String str = clazz.getName();
		return str.substring(str.lastIndexOf('.') + 1);
	}

	/**
	 * 在左边填充字符串.
	 * 
	 * @param rString
	 *            要填充的初始字符串
	 * @param rLength
	 *            填充后的长度
	 * @param rPad
	 *            填充字符
	 * @return String
	 */
	public static String padLeading(final String rString, final int rLength,
			final String rPad) {
		String lTmpPad = "";

		final String lTmpStr = StrUtil.strnull(rString);

		if (lTmpStr.length() >= rLength) {
			return lTmpStr.substring(0, lTmpStr.length());
		} else {
			for (int gCnt = 1; gCnt <= rLength - lTmpStr.length(); gCnt++) {
				lTmpPad = lTmpPad + rPad;
			}
		}
		return lTmpPad + lTmpStr;
	}

	/**
	 * 去除制表符,换行 \\s*|\t|\r|\n可以去除空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removeTabAndEnter(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 判断字符串中是否包含空格
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkBlank(String str) {
		boolean flag = false;
		if (str != null) {
			Pattern pattern = Pattern.compile("[\\s]+");
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 判断字符串中是否包含小写字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkLowerCase(String str) {
		boolean flag = false;
		if (str != null) {
			Pattern pattern = Pattern.compile("[a-z]+");
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 朱林涛 2014年06月09日 根据实际开发于2009年9月7日最新统计：
	 * 中国电信发布中国3G号码段:中国联通185,186;中国移动188,187;中国电信189,181,180共7个号段。
	 * 3G业务专属的180-189号段已基本分配给各运营商使用,其中180、181、189分配给中国电信,187、188归中国移动使用,185、186属于新联通。
	 * 中国移动拥有号码段：139、138、137、136、135、、134、159、158、157（3G）、152、151、150、188（3G）、187（3G）,14个号段;
	 * 中国联通拥有号码段：130、131、132、155、156（3G）、186（3G）、185（3G）,6个号段;
	 * 中国电信拥有号码段：133、153、189（3G）、180（3G）,4个号码段 ;
	 * 虚拟运营商拥有号码段：17开头的手机号码段，具体是哪几个部分还未确定。
	 * 移动:2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150
	 * 3G号段(TD-SCDMA网络)有157,182,183,188,187 147是移动TD上网卡专用号段. 
	 * 联通:2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185
	 * 电信:2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,181,180
	 * 虚拟运营商有17开头的手机号码段，具体是哪几个部分还未确定。
	 */
	public static boolean checkTelephoneNumber(String telephoneNumber) {
		/*Pattern p = Pattern
				.compile("^((13[0-9])|147|(15[^4,\\D])|(18[0,1,2,3,5-9]))\\d{8}$");
		Matcher m = p.matcher(telephoneNumber);
		return m.matches();*/
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(telephoneNumber);
		if(m.matches()){
			/*List<NumberSegment> list = numberSegmentManager.findAllByActive();
			for(NumberSegment ns : list){
				if(telephoneNumber.startsWith(ns.getNumberSegment())){
	            	return true;
	            }
			}*/
			return true;
		}
		return false;
		// return InputFieldUtil.checkPhone1(telephoneNumber);
	}
	/**
	 * 判断字符串中是否全是汉字
	 * @param str
	 * @return
	 */
	public static boolean checkStringContainChinese(String str){
		boolean result=false;
		if(str!=null){
			String reg="[\\u4e00-\\u9fa5]+";//全为汉字正则
			result=str.matches(reg);			
		}
		return result;	
	}
	
	/**
	 * 判断字符
	 * @param str
	 * @return
	 */
	public static boolean checkStr(String str){
		boolean result=false;
		if(str!=null){
			String regex = "^[a-z0-9A-Z]{8}+$";//可以是纯数字、或者纯字母、或者数字和字母组合8位
			result=str.matches(regex);
		}
		
		return result;
		
	}
	/**
	 * 检查是否匹配集团邮箱xxxxxxx@chinatelecom.cn  
	 * @param str
	 * @return
	 */
	public static boolean checkMail(String str){
		boolean result=false;
		if(str!=null){
			 String regEx = "^[a-z0-9A-Z]{1,}@(chinatelecom.cn)";
			result=str.matches(regEx);
		}
		return result;
		
	}
	
  
    public static String getNodeVal(String subStr, String nodeName){
        if (isNullOrEmpty(subStr) || isNullOrEmpty(nodeName)) {
            return "";
        }
        String startStr = "<" + nodeName + ">";
        String endStr = "</" + nodeName + ">";
        return getXmlContent(subStr, startStr, endStr);
    }
	

    public static String getXmlContent(String inXML, String maskStartStr, String maskEndStr) {
        String outStr = "";
        int startIndex = -1;
        int endIndex = -1;
        
        if (inXML != null) {
            startIndex = inXML.indexOf(maskStartStr);
            endIndex = inXML.indexOf(maskEndStr);
            
            if (startIndex != -1) {
                int contentStart = inXML.indexOf('>', startIndex) + 1;
                outStr = inXML.substring(contentStart, endIndex);
            }
        }
        
        return outStr;
    }
}
