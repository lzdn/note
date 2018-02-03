package com.lzdn.note.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

	/**
	 * 得到指定类型的泛型实参
	 * 
	 * @param clazz
	 * @param <T>
	 * @return {@link Class}
	 */
	public static <T> Class<T> findParameterizedType(Class<?> clazz) {
		Type parameterizedType = clazz.getGenericSuperclass();
		// CGLIB subclass target object(泛型在父类上)
		if (!(parameterizedType instanceof ParameterizedType)) {
			parameterizedType = clazz.getSuperclass().getGenericSuperclass();
		}
		if (!(parameterizedType instanceof ParameterizedType)) {
			return null;
		}
		Type[] actualTypeArguments = ((ParameterizedType) parameterizedType)
				.getActualTypeArguments();
		if (actualTypeArguments == null || actualTypeArguments.length == 0) {
			return null;
		}
		return (Class<T>) actualTypeArguments[0];
	}

}
