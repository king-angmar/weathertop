package xyz.wongs.weathertop.war3.web.utils;

/**
 * @author WCNGS@QQ.CO
 * @version V1.0
 * @Title:
 * @Package spring-cloud xyz.wongs.tools.zonecode
 * @Description: TODO
 * @date 2018/7/1 1:03
 **/
public class ZoneCodeStringUtils {


    public static String RESULT_KEY_FLAG = "flag";
    public static String RESULT_KEY = "location";

    /**
     * 方法实现说明
     * @method      interceptionStringByLastIndexOf
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param str
     * @param suffix
     * @return      java.lang.String
     * @exception
     * @date        2018/7/1 1:03
     */
    public static String interceptionStringByLastIndexOf(String str ,String suffix){

        int i = str.lastIndexOf(suffix);
        return str.substring(0,i);
    }

    /**
     * 方法实现说明
     * @method      getUrlStrByLocationCode
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param locationCode
     * @param level
     * @return      String
     * @exception
     * @date        2018/7/1 9:33
     */
    public static String getUrlStrByLocationCode(String locationCode,int level){

        int bit = (level-1)*2;
        String subStr = locationCode.substring(0,bit);
        char[] ch= subStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <bit ; i++) {
            sb.append(ch[i]);
            if(i!=0 && (i+1)%2==0){
                sb.append("/");
            }
        }
        return sb.toString();
    }
}
