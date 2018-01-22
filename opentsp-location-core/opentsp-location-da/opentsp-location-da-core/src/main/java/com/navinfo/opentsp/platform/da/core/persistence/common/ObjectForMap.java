package com.navinfo.opentsp.platform.da.core.persistence.common;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将对象Object转化可远程传输的HashMap
 *
 * @author Administrator
 *
 */
public class ObjectForMap {

	/**
	 * 将list列表 转换为可远程传输的 MAP List列表
	 *
	 * @param list
	 * @return
	 * @throws ObjectForMapException
	 */
	public static List<Map<String, Object>> objectForMapByList(List<Object> list)
			throws ObjectForMapException {
		List<Map<String, Object>> _templist = new ArrayList<Map<String, Object>>();
		for (Object ob : list) {
			_templist.add(objectForMap(ob));
		}
		return _templist;
	}

	/**
	 * 转换所有的对象
	 *
	 * @param obj
	 * @return
	 * @throws ObjectForMapException
	 */
	public static Map<String, Object> objectForMap(Object obj)
			throws ObjectForMapException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				String MethodName = ObjectForMap.getMethodNameByFieldName(field
						.getName());
				Object object = ObjectForMap.getMethodResuletByMethodName(obj,
						MethodName);
				map.put(field.getName(), object);
			}
			return map;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DBEntity dbObjectForMap(Object obj)
			throws ObjectForMapException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			DBEntity dbEntity = new DBEntity(fields.length);
			LCTable lcTable = obj.getClass().getAnnotation(LCTable.class);
			if(lcTable != null)
				dbEntity.setTableName(lcTable.name());
			for (Field field : fields) {
				//寻找主键
				LCPrimaryKey primaryKey = field.getAnnotation(LCPrimaryKey.class);
				if(primaryKey != null){
					String MethodName = ObjectForMap.getMethodNameByFieldName(field
							.getName());
					Object object = ObjectForMap.getMethodResuletByMethodName(obj,
							MethodName);
					map.put(field.getName(), object);
					dbEntity.setPrimaryKeyName(field.getName());
					dbEntity.setPrimaryKeyValue(Integer.parseInt(String.valueOf(object==null?"0":object)));
					continue;
				}
				// 通过注解,过滤不需要存储字段
				LCTransient filed = field.getAnnotation(LCTransient.class);
				if (filed != null) {
					continue;
				}
				String MethodName = ObjectForMap.getMethodNameByFieldName(field
						.getName());
				Object object = ObjectForMap.getMethodResuletByMethodName(obj,
						MethodName);
				dbEntity.addField(field.getName(), object);
			}
			return dbEntity;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<DBEntity> dbObjectForMapByList(
			List<Object> list) throws ObjectForMapException {
		List<DBEntity> _templist = new ArrayList<DBEntity>();
		for (Object ob : list) {
			_templist.add(dbObjectForMap(ob));
		}
		return _templist;
	}

	/**
	 * 根据字段名取得get方法的名字
	 *
	 * @param fieldName
	 * @return
	 */
	private static String getMethodNameByFieldName(String fieldName) {
		if (fieldName != null && fieldName.length() > 0) {
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
					+ "" + fieldName.substring(1);
			return methodName;
		}
		return null;

	}

	/**
	 * 根据方法名调用方法取得结果
	 *
	 * @param obj
	 * @param methodName
	 * @return
	 * @throws ObjectForMapException
	 */
	private static Object getMethodResuletByMethodName(Object obj,
			String methodName) throws ObjectForMapException {
		try {
			Method method = obj.getClass().getMethod(methodName);
			Object object = method.invoke(obj);
			return object;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ObjectForMapException("由安全管理器抛出的异常，指示存在安全侵犯.");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ObjectForMapException("无法找到某一特定方法时，抛出该异常.");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ObjectForMapException("抛出的异常表明向方法传递了一个不合法或不正确的参数.");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ObjectForMapException("当前正在执行的方法无法访问指定类、字段、方法或构造方法的定义.");
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ObjectForMapException("调用方法获取结果错误.");
		}
	}

	/**
	 * 测试
	 * 
	 * @param args
	 * @throws ObjectForMapException
	 */
	public static void main(String[] args) throws ObjectForMapException {
		LcDictDBEntity dict = new LcDictDBEntity();
		dict.setDict_id(1);
		dict.setDict_code(100);
		dict.setDict_name("test");

		System.err.println(dbObjectForMap(dict));
	}

}
