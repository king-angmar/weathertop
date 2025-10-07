
package xyz.wongs.weathertop.base.utils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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
 * @ClassName Reflections
 * @Description 反射工具类.
 *  提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * @author WCNGS@QQ.COM
 * @date 2019/2/27 17:20
 * @Version 1.0.0
*/
@SuppressWarnings("rawtypes")
public class Reflections {
	
	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static String INTEGER = "Integer";
	public static String BIGDECIMAL = "BigDecimal";
	public static String LONG = "Long";
	public static String DATE = "Date";
	public static String INT = "int";
	private static Logger logger = LoggerFactory.getLogger(Reflections.class);

	/**
	 * 调用Getter方法.
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")){
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}
	
	public static Class<?> getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}
	
	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}

	public static List listCoverVos(List sourceList, Class clazz) {
		List targetList = null;
		targetList = new ArrayList<>(sourceList.size());
		for (int i = 0; i < sourceList.size(); i++) {
			Object source = sourceList.get(i);
			Object target = getObjInstance(clazz);
			coverBean2Bean(source, target);
			targetList.add(target);
		}
		return targetList;
	}

	/** 动态利用对象的构造函数暴力构造新对象
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/3/5 14:32
	 * @param clazz
	 * @return java.lang.Object
	 * @throws
	 * @since
	 */
	public static Object getObjInstance(Class clazz){
		try {
			Constructor constructor = clazz.getConstructor(null);
			Object o = constructor.newInstance(null);
			return o ;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}



	/** Exchange content values between beans
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/3/5 11:24
	 * @param source
	 * @param target
	 * @return void
	 * @throws
	 * @since
	 */
	public static void coverBean2Bean(Object source, Object target){

		Class<?> sourceClz = source.getClass();
		Field[] sourceFields = sourceClz.getDeclaredFields();
		Method[] sourceMethods = sourceClz.getDeclaredMethods();

		/**
		 * 目标对象
		 */
		Class<?> targetCls = target.getClass();
		Field[] targetFields = targetCls.getDeclaredFields();
		Method[] targetMethods = targetCls.getDeclaredMethods();

		try {
			for(Field targField:targetFields){
				String targFieldName = targField.getName();
				String targFieldType = targField.getType().getSimpleName();

				for(Field sourceField:sourceFields){

					String sourceFieldType = sourceField.getType().getSimpleName();
					String sourceFieldName = sourceField.getName();

					//判断属性名与属性的类型一致
					if(targFieldName.equals(sourceFieldName) && targFieldType.equals(sourceFieldType)){
						String fieldGetName = xyz.wongs.weathertop.base.utils.StringUtils.parGetName(sourceField.getName());
						if (!xyz.wongs.weathertop.base.utils.StringUtils.checkGetMet(sourceMethods, fieldGetName)) {
							continue;
						}
						Method sourceFieldGetMet = sourceClz.getMethod(fieldGetName);
						Object value = sourceFieldGetMet.invoke(source);
						if(null==value || xyz.wongs.weathertop.base.utils.StringUtils.isBlank(value.toString())){
							continue;
						}
						Object val = getValue(value.toString(),targFieldType);
						//Set Method
						String fieldSetName = xyz.wongs.weathertop.base.utils.StringUtils.parSetName(targField.getName());
						if (!xyz.wongs.weathertop.base.utils.StringUtils.checkGetMet(targetMethods, fieldSetName)) {
							continue;
						}
						Method fieldSetMet = targetCls.getMethod(fieldSetName, new Class[] {targField.getType()});
						fieldSetMet.invoke(target, new Object[] {val});
					}
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e){
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		} catch (InvocationTargetException e){
			e.printStackTrace();
		}
	}

	/**
	 * @author WCNGS@QQ.COM
	 * @See
	 * @date 2019/3/5 13:41
	 * @param value
	 * @param fieldType
	 * @return java.lang.Object
	 * @throws
	 * @since
	 */
	public static Object getValue(String value ,String fieldType){

		Object val = null;
		try {
			if (INTEGER.equals(fieldType)) {
				val=Integer.parseInt(value);
			} else if(BIGDECIMAL.equals(fieldType)) {
				val=new BigDecimal(value);
			} else if(LONG.equals(fieldType)) {
				val=Long.valueOf(value);
			} else if(DATE.equals(fieldType)) {
				val = DateUtils.parseDate(value);
			} else if(INT.equals(fieldType)) {
				val = Integer.parseInt(value);
			} else{
				val=value;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return val;
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
			Object value;
			try {
				String fieldType = fields[i].getType().getSimpleName();
				String fieldGetName = xyz.wongs.weathertop.base.utils.StringUtils.parGetName(fields[i].getName());
				//校验是否有GETTER、SETTER的方法
				if (!xyz.wongs.weathertop.base.utils.StringUtils.checkGetMet(methods, fieldGetName)) {
					continue;
				}
				//Type conversion
				value = getValue(s[i],fieldType);
				Method fieldSetMet = cls.getMethod(fieldGetName, new Class[] {fields[i].getType()});
				fieldSetMet.invoke(bean, new Object[] {value});
			} catch (Exception e) {
				continue;
			}
		}

		return num;
	}




	public static void getField(Object bean,List<String> retFields){
		try {
			Class<?> cls = bean.getClass();
			Field[] fields = cls.getDeclaredFields();
			Method[] methods = cls.getDeclaredMethods();
			for (int i=0;i<fields.length;i++) {
				String fieldGetName = xyz.wongs.weathertop.base.utils.StringUtils.parGetName(fields[i].getName());
				//校验是否有GETTER、SETTER的方法
				if (!xyz.wongs.weathertop.base.utils.StringUtils.checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldSetMet = cls.getMethod(fieldGetName);
				Object o =fieldSetMet.invoke(bean);
				//Type conversion
				if(null!=o){
					retFields.add(fields[i].getName());
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
	}
}
