package xyz.wongs.weathertop.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @Description :
 * @author: WCNGS@QQ.COM
 * @create: 2017/12/29 13:12
 * @Modified By :
 **/
public class FileDocUtil {

    private static Logger logger = LoggerFactory.getLogger(FileDocUtil.class);

    /**
     * 换行字符
     */
    private static final String ENTER_KEY_1= "\r\n";
    private static final String ENTER_KEY_2= "\n";

    /**
     *
     */
    public static final int SCAN_NUMBER = 5 * 60;

    public static final int ONE_SECOND = 1000;


    /**
     * 每次休眠时间
     */
    public static final int SCAN_SECOND = 10;



    /**
     * 方法实现说明
     * @method      compBean
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param bean   组装的Bean
     * @param str   字符
     * @param isCheck
     * @param regex   分割字符
     * @return      int
     * @exception
     * @date        2018/4/9 10:22
     */
    public static int compBean(Object bean,String str,Boolean isCheck,String regex){
        String[] s = str.split(regex);
        return FileDocUtil.getFieldValueMap(s,bean,isCheck);
    }

    /**
     * 方法实现说明：替换字符串中的换行：\r\n、\n返回一个新的字符串
     * @method      repalceEnterKey
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param str
     * @return      java.lang.String
     * @exception
     * @date        2018/4/9 11:01
     */
    public static String repalceEnterKey(String str){
        if(str.contains(ENTER_KEY_1)){
            str = str.replace("\r\n","");
        }
        if(str.contains(ENTER_KEY_2)){
            str = str.replace("\n","");
        }
        return str;
    }

    /**
     * 方法实现说明
     * @method      compBean
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param bean   组装的Bean
     * @param str   字符
     * @param regex   分割字符
     * @return      int
     * @exception
     * @date        2018/4/9 10:22
     */
    public static int compBean(Object bean,String str,String regex){
        return compBean(bean,str,false,regex);
    }

    /**
     * 方法实现说明
     * @method      getFieldValueMap
     * @author      WCNGS@QQ.COM
     * @version
     * @see
     * @param s
     * @param bean
     * @param isCheck
     * @return      int
     * @exception
     * @date        2018/4/9 10:24
     */
    public static int getFieldValueMap(String[] s,Object bean,Boolean isCheck){

        int num = 0;
        // 取出bean里的所有方法
        Class<?> cls = bean.getClass();
        Field[] fields = cls.getDeclaredFields();
        /**
         * 此处判断下发表字段数量
         */
        if(isCheck && s.length > fields.length){
            num = s.length - fields.length;
        }
        Method[] methods = cls.getDeclaredMethods();
        for (int i=0;i<s.length;i++) {
            Object value = null;
            try {
                String fieldType = fields[i].getType().getSimpleName();
                String fieldSetName = StringUtils.parSetName(fields[i].getName());
                //校验是否有GETTER、SETTER的方法
                if (!StringUtils.checkGetMet(methods, fieldSetName)) {
                    continue;
                }
                //Type conversion
                if ("Integer".equals(fieldType)) {
                    value=Integer.valueOf(s[i]);
                } else if("BigDecimal".equals(fieldType)) {
                    value=new BigDecimal(s[i]);
                } else if("Long".equals(fieldType)) {
                    value=Long.valueOf(s[i]);
                } else if("Date".equals(fieldType)) {
                    value = DateUtils.parseDate(s[i]);
                } else if("int".equals(fieldType)) {
                    value = Integer.parseInt(s[i]);
                } else{
                    value=s[i];
                }
                Method fieldSetMet = cls.getMethod(fieldSetName, new Class[] {fields[i].getType()});
                fieldSetMet.invoke(bean, new Object[] {value});
            } catch (Exception e) {
                logger.error(bean.getClass().getName()+" ;动态赋值异常=>"+s[i]);
                continue;
            }
        }

        return num;
    }
}
