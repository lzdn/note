package com.lzdn.note.util;

import java.lang.reflect.Field;
import javax.persistence.Id;

public class ReflectHelper {

	/**
	 * 获取实体类的字段信息
	 * 
	 * @param clazz
	 *            实体类
	 * @return 字段集合
	 */
	public static Field getIdField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Field item = null;
		for (Field field : fields) {
			// 获取实体类中标识@Id的字段
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				field.setAccessible(true);
				item = field;
				break;
			}
		}
		if (item == null) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				item = getIdField(superclass);
			}
		}
		return item;

	}
}
