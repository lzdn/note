package com.lzdn.note.common.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.lzdn.note.util.ReflectHelper;
import com.lzdn.note.util.UUIDUtil;

@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements BaseRepository<T, ID> {

	/*
	 * private final Class<T> domainClass;
	 * 
	 * public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager)
	 * { super(domainClass, entityManager); this.domainClass = domainClass; }
	 * 
	 * @Override public boolean support(String modelType) { return
	 * domainClass.getName().equals(modelType); }
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(BaseRepositoryImpl.class);
	/**
	 * 持久化上下文
	 */
	private final EntityManager entityManager;
	//private final Class<T> domainClass;

	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	//	this.domainClass = domainClass;
		this.entityManager = em;
	}

	@Override
	public void store(Object... item) {
		if (null != item) {
			for (Object entity : item) {
				innerSave(entity);
			}
		}
	}

	@Override
	public void update(Object... item) {
		if (null != item) {
			for (Object entity : item) {
				entityManager.merge(entity);
			}
		}
	}

	@Override
	public int executeUpdate(String qlString, Object... values) {
		Query query = entityManager.createQuery(qlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i + 1, values[i]);
			}
		}
		return query.executeUpdate();
	}

	@Override
	public int executeUpdate(String qlString, Map<String, Object> params) {
		Query query = entityManager.createQuery(qlString);
		for (String name : params.keySet()) {
			query.setParameter(name, params.get(name));
		}
		return query.executeUpdate();
	}

	@Override
	public int executeUpdate(String qlString, List<Object> values) {
		Query query = entityManager.createQuery(qlString);
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		return query.executeUpdate();
	}

	/**
	 * 保存对象
	 * 
	 * @param item
	 *            保存对象
	 * @return
	 */
	private Serializable innerSave(Object item) {
		try {
			if (item == null)
				return null;
			Class<?> clazz = item.getClass();
			Field idField = ReflectHelper.getIdField(clazz);
			Method getMethod = null;
			if (idField != null) {
				Class<?> type = idField.getType();
				Object val = idField.get(item);
				if (type == String.class && (val == null || "".equals(val))) {
					idField.set(item, UUIDUtil.uuid());
				}
			} else {
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					Id id = method.getAnnotation(Id.class);
					if (id != null) {
						Object val = method.invoke(item);
						if (val == null || "".equals(val)) {
							String methodName = "s" + method.getName().substring(1);
							Method setMethod = clazz.getDeclaredMethod(methodName, method.getReturnType());
							if (setMethod != null) {
								setMethod.invoke(item, UUIDUtil.uuid());
							}
						}
						getMethod = method;
						break;
					}
				}
			}
			entityManager.persist(item);
			entityManager.flush();
			if (idField != null) {
				return (Serializable) idField.get(item);
			}
			if (getMethod != null) {
				return (Serializable) getMethod.invoke(item);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
