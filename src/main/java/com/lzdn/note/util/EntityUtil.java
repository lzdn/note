package com.lzdn.note.util;

import javax.persistence.Table;

public class EntityUtil {

	/**
	 * 获取使用JPA注解的实体类所对应的表名
	 * 
	 * @param clazz
	 * @return {@link String}
	 */
	private static String getTableName(Class clazz) {
		Table annotation = (Table) clazz.getAnnotation(Table.class);
		if (annotation != null) {
			return annotation.name();
		}
		return null;
	}

}
