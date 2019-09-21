package xyz.wongs.weathertop.handball.utils;

import xyz.wongs.weathertop.base.utils.DateUtils;
import xyz.wongs.weathertop.base.utils.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author WCNGS@QQ.CO
 * @version V1.0
 * @Title:
 * @Package spring-cloud xyz.wongs.tools.utils
 * @Description: TODO
 * @date 2018/7/4 20:54
 **/
public class MethodUtil {

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
            Object value;
            try {
                String fieldType = fields[i].getType().getSimpleName();
                String fieldGetName = StringUtils.parGetName(fields[i].getName());
                //校验是否有GETTER、SETTER的方法
                if (!StringUtils.checkGetMet(methods, fieldGetName)) {
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
                Method fieldSetMet = cls.getMethod(fieldGetName, new Class[] {fields[i].getType()});
                fieldSetMet.invoke(bean, new Object[] {value});
            } catch (Exception e) {
                continue;
            }
        }

        return num;
    }

    public static int getFieldValue(Object bean, Root<?> root, CriteriaBuilder cb, List<Predicate> lp){
        try {
            Class<?> cls = bean.getClass();
            Field[] fields = cls.getDeclaredFields();
            Method[] methods = cls.getDeclaredMethods();
            for (int i=0;i<fields.length;i++) {
                String fieldType = fields[i].getType().getSimpleName();
                String fieldGetName = StringUtils.parGetName(fields[i].getName());
                //校验是否有GETTER、SETTER的方法
                if (!StringUtils.checkGetMet(methods, fieldGetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldGetName);
                Object o =fieldSetMet.invoke(bean);
                //Type conversion
                if(null!=o){
                    String value = o.toString();
                    if ("Integer".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(Integer.class), Integer.valueOf(value)));
                    } else if("BigDecimal".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(BigDecimal.class), new BigDecimal(value)));
                    } else if("Long".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(Long.class), Long.valueOf(value)));
                    } else if("Date".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(Date.class), DateUtils.parseDate(value)));
                    } else if("int".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(Integer.class), Integer.valueOf(value)));
                    } else if("String".equals(fieldType)) {
                        lp.add(cb.equal(root.get(fields[i].getName()).as(String.class), value));
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
