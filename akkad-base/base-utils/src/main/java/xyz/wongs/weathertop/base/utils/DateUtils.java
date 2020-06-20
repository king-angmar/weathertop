/**
 * Copyright &copy; 2012-2016 <a href="https://wongs.xyz">UECC</a> All rights reserved.
 */
package xyz.wongs.weathertop.base.utils;

import com.xiaoleilu.hutool.date.DateField;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author WCNGS@QQ.COM
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static String getBeforeTime(int minute) {
		Date newDate = com.xiaoleilu.hutool.date.DateUtil.offset(new Date(), DateField.MINUTE, -minute);
		return com.xiaoleilu.hutool.date.DateUtil.formatDateTime(newDate);
	}

	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


	public static final String dateTimeNow(final String format)
	{
		return parseDateToStr(format, new Date());
	}

	public static final String parseDateToStr(final String format, final Date date)
	{
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） patternDateUtil.java可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	public static final Date DEFAULT_DATE = DateUtils.parseByDayPattern("1970-01-01");

	public static final String DAY_PATTERN = "yyyy-MM-dd";
	public static Date parseByDayPattern(String str) {
        return parseDate(str, DAY_PATTERN);
    }

	public static Date parseDate(String str, String pattern) {
		try {
			return parseDate(str, new String[]{pattern});
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String formatByDateTimePattern(Date date) {
        return DateFormatUtils.format(date, DATETIME_PATTERN);
    }
	/**
	 * 根据格式产生时间
	 * @param format
	 * @return
	 */
	public static String getDateTime(String format) {
		return formatDate(new Date(), format);
	}
	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*60*1000);
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}


	/**
	 * @Author: WCNGS@QQ.COM
	 * @Date: 2017/12/22 9:10
	 * @Description:
	 * @param i	与当天的偏移，负数 则是往前
	 * @Mod:
	 */
	public static String getDaySimple(int i) {
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,i);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyyMMdd").format(time);
	}

	/**
	 * 日期时间偏移
	 * offset = 1,date=2018-11-02 16:47:00
	 * 结果：2018-11-03 16:47:00
	 * @param date
	 * @param offset
	 * @return
	 */
	public static Date offset(Date date, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return cal.getTime();
	}

	/**日期偏移
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/9/25 18:01
	 * @param date
	 * @param offset
	 * @param calendar
	 * @return java.util.Date
	 * @throws
	 * @since
	 */
	public static Date offset(Date date, int offset,int calendar) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendar, offset);
		return cal.getTime();
	}

	/**
	 * @Author: WCNGS@QQ.COM
	 * @Date: 2017/12/22 9:10
	 * @Description:
	 * @param i	与当前月份的偏移，负数 则是往前
	 * @Mod:
	 */
	public static String getMonth(int i) {
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH,i);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyyMM").format(time);
	}

	public static boolean isValidDate(String str,String pattern) {
		boolean convertSuccess=true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
		// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
		// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess=false;
		}
		return convertSuccess;
	}

	/**
	 * 根据输入字符串，转换日期
	 * @method      getDatebystr
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param str
	 * @param pattern
	 * @return      java.utils.Date
	 * @exception
	 * @date        2018/4/12 19:55
	 */
	public static Date getDatebystr(String str,String pattern){

		if(str==null||str==""){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 方法实现说明
	 * @method      dateVali
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param date1
	 * @param date2
	 * @return      int
	 * @exception
	 * @date        2018/4/12 19:53
	 */
	public static int dateVali(Date date1,Date date2){

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		//同一年
		if(year1 != year2) {
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)  {
				//闰年
				if(i%4==0 && i%100!=0 || i%400==0) {
					timeDistance += 366;
				}else {
					//不是闰年
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		} else  {
			return day2-day1;
		}
	}


	/**
	 * 方法实现说明
	 * @method      dateVali
	 * @author      WCNGS@QQ.COM
	 * @version
	 * @see
	 * @param date1
	 * @param date2
	 * @return      int
	 * @exception
	 * @date        2018/4/12 19:53
	 */
	public static int dateValiSeconds(Date date1,Date date2){

//		Calendar cal1 = Calendar.getInstance();

		long time1 = date1.getTime();


		long time2 = date2.getTime();
		long between_days=(time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}

    public static java.sql.Date util2sql(java.util.Date aDate) {
        if (aDate == null) {
            return null;
        }
        return new java.sql.Date(aDate.getTime());
    }


//	public static void main(String[] args) {
//		System.out.println(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
//	}

	/**获取上n个小时整点小时时间
	 * @param date
	 * @return
	 */
	public static String getLastHourTime(Date date,int n){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY)-n);
		date = ca.getTime();
		return sdf.format(date);
	}
	/**
	 * @param date
	 * @return
	 */

	/** 获取当前时间的整点小时时间，默认格式为yyyy-MM-dd HH:mm:ss
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/10/23 23:59
	 * @param date 必填
	 * @param pattern 可为空
	 * @return java.lang.String
	 * @throws
	 * @since
	 */
	public static String getCurrHourTime(Date date,String pattern){

		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		date = ca.getTime();
		if(null==pattern){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}


	/** 判断两个日期是否相等
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/10/23 23:58
	 * @param date1
	 * @param date2
	 * @return boolean
	 * @throws
	 * @since
	 */
	public static boolean isSameDate(Date date1, Date date2) {

		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);

			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
					.get(Calendar.YEAR);
			boolean isSameMonth = isSameYear
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
			boolean isSameDate = isSameMonth
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
					.get(Calendar.DAY_OF_MONTH);

			return isSameDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
