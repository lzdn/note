package com.lzdn.note.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.lzdn.note.common.support.Condition;
import com.lzdn.note.common.support.SearchRequest;
import com.lzdn.note.common.support.Sort;
import com.lzdn.note.common.support.page.Pageable;
import com.lzdn.note.common.support.repository.EnabledQueryCache;
import com.lzdn.note.common.support.search.SearchFilter;
import com.lzdn.note.common.support.search.Searchable;
import com.lzdn.note.util.ReflectUtil;
import com.lzdn.note.util.StringPool;

public abstract class BaseDao<E> implements Dao<E> {

	private SessionFactory sessionFactory;
	private Class<E> entityClass;
	private boolean enabledQueryCache = false;

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public BaseDao() {
		this.entityClass = ReflectUtil.findParameterizedType(getClass());

		// 根据注解决定是否启动Hibernate 查询缓存
		EnabledQueryCache enabledQueryCacheAnnotation = AnnotationUtils.findAnnotation(this.entityClass,
				EnabledQueryCache.class);
		if (enabledQueryCacheAnnotation != null) {
			this.enabledQueryCache = enabledQueryCacheAnnotation.value();
		}
	}

	@Override
	public Serializable save(E entity) {
		return getSession().save(entity);
	}

	@Override
	public void persist(E entity) {
		getSession().persist(entity);
	}

	@Override
	public void delete(E entity) {
		getSession().delete(entity);
	}

	@Override
	public boolean deleteById(Serializable... id) {
		boolean result = false;
		if ((id != null) && (id.length > 0)) {
			for (int i = 0; i < id.length; i++) {
				E entity = get(id[i]);
				if (entity != null) {
					getSession().delete(entity);
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public void update(E entity) {
		getSession().update(entity);
	}

	@Override
	public void update(Searchable searchable, String[] propName, Object[] propValue) {
		if (propName == null || propName.length == 0 || propValue == null || propValue.length == 0
				|| propName.length != propValue.length) {
			throw new IllegalArgumentException("The given property names must not be null or empty.");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("update " + this.entityClass.getName() + " e set ");
		// 拼接需要更新的字段属性名
		for (int i = 0; i < propName.length; i++) {
			sb.append(propName[i] + " = :v_" + propName[i] + ",");
		}
		sb.deleteCharAt(sb.length() - 1);

		// 处理条件
		if (searchable != null && searchable.hasSearchFilter()) {
			sb.append(" where ");
			prepareHql(sb, searchable);
		}

		Query query = getSession().createQuery(sb.toString());
		// 拼接需要更新的字段属性值
		for (int i = 0; i < propName.length; i++) {
			query.setParameter("v_" + propName[i], propValue[i]);
		}

		// 处理条件值
		if (searchable != null && searchable.hasSearchFilter()) {
			prepareValues(query, searchable);
		}
		query.executeUpdate();
	}

	@Override
	public void update(Searchable searchable, String propName, Object propValue) {
		update(searchable, new String[] { propName }, new Object[] { propValue });
	}

	@Override
	public void evict(E entity) {
		getSession().evict(entity);
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public E merge(E entity) {
		return (E) getSession().merge(entity);
	}

	@Override
	public E get(Serializable id) {
		return getSession().get(this.entityClass, id);
	}

	@Override
	public E load(Serializable id) {
		return getSession().load(this.entityClass, id);
	}

	@Override
	public E get(Searchable searchable) {
		Assert.notNull(searchable, "Searchable must be not null.");

		// 初始化SQL
		StringBuilder sb = new StringBuilder("select e from " + this.entityClass.getName() + " e");

		if (searchable.hasSearchFilter()) {
			sb.append(" where ");
			prepareHql(sb, searchable);
		}

		if (searchable.hashSort()) {
			prepareOrder(sb, searchable.getSort());
		}

		Query query = getSession().createQuery(sb.toString());
		enableQueryCache(query);
		if (searchable.hasSearchFilter()) {
			prepareValues(query, searchable);
		}

		List<E> list = query.list();
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public E get(String propName, Object propValue) {
		Searchable searchable = SearchRequest.newSearchRequest();
		searchable.addSearchParam(propName, propValue);
		return get(searchable);
	}

	@Override
	public E get(Map<String, Object> searchParams) {
		Searchable searchable = SearchRequest.newSearchRequest();
		searchable.addSearchParams(searchParams);
		return get(searchable);
	}

	@Override
	public Long countAll() {
		return (Long) getSession().createQuery("select count(*) from " + this.entityClass.getName()).uniqueResult();
	}

	/**
	 * 使用Hibernate Criteria实现
	 * 
	 * @param sort
	 *            排序
	 * @param maxResults
	 *            需要返回的最大行数
	 * @return
	 */
	@Override
	public List<E> findAll(Sort sort, Integer maxResults) {
		Criteria criteria = getSession().createCriteria(this.entityClass);
		enableQueryCache(criteria);
		/*
		 * 在被查询实体有left join操作时，该设置被用来进行重复数据的合并
		 * 但是由此会引发另外一个问题，即：hibernate的元查询maxResults在查询时就已确定，而distinct
		 * entity是对实体的操作 所以会导致返回的结果与maxResults预期不符，引发分页问题 这里给出两种解决方法： 1.
		 * （推荐）取消实体之间的非必要join，只在用到的时候才去查询子实体，这样做的好处是既不影响分页，又提升了查询性能，
		 * 不好的地方是可能需要多次查询 2. 仍然使用该方法，但是在有实体间left join时不进行分页
		 */
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (sort != null && sort.isNotEmpty()) {
			for (Sort.Order order : sort) {
				if (Sort.Direction.desc == order.getDirection()) {
					criteria.addOrder(Order.desc(order.getProperty()));
				} else if (Sort.Direction.asc == order.getDirection()) {
					criteria.addOrder(Order.asc(order.getProperty()));
				}
			}
		}
		if (maxResults != null) {
			criteria.setFirstResult(0);
			criteria.setMaxResults(maxResults);
		}
		return criteria.list();
	}

	@Override
	public List<E> findAll(Integer maxResults) {
		return findAll(null, maxResults);
	}

	@Override
	public List<E> findAll() {
		return findAll(null, null);
	}

	@Override
	public Long count(Searchable searchable) {
		Assert.notNull(searchable, "Searchable must be not null.");

		// 初始化SQL
		StringBuilder sb = new StringBuilder("select count(*) from " + this.entityClass.getName() + " e");

		if (searchable.hasSearchFilter()) {
			sb.append(" where ");
			prepareHql(sb, searchable);
		}

		Query query = getSession().createQuery(sb.toString());
		enableQueryCache(query);

		if (searchable.hasSearchFilter()) {
			prepareValues(query, searchable);
		}

		return (Long) query.uniqueResult();
	}

	@Override
	public List<E> find(Searchable searchable) {
		Assert.notNull(searchable, "Searchable must be not null.");

		// 初始化SQL
		StringBuilder sb = new StringBuilder("select e from " + this.entityClass.getName() + " e");

		if (searchable.hasSearchFilter()) {
			sb.append(" where ");
			prepareHql(sb, searchable);
		}

		if (searchable.hashSort()) {
			prepareOrder(sb, searchable.getSort());
		}

		Query query = getSession().createQuery(sb.toString());
		enableQueryCache(query);
		if (searchable.hasSearchFilter()) {
			prepareValues(query, searchable);
		}

		if (searchable.hasPageable()) {
			Pageable pageable = searchable.getPage();
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
		}

		return query.list();
	}

	@Override
	public List<E> find(Map<String, Object> searchParams) {
		Searchable searchable = SearchRequest.newSearchRequest();
		searchable.addSearchParams(searchParams);
		return find(searchable);
	}

	@Override
	public List<E> find(String propName, Object propValue) {
		Searchable searchable = SearchRequest.newSearchRequest();
		searchable.addSearchParam(propName, propValue);
		return find(searchable);
	}

	/**
	 * <p>
	 * 预处理HQL语句
	 * </p>
	 * 根据基本hql生成包含预处理查询参数的hql语句
	 * 
	 * @param hql
	 * @param searchable
	 */
	protected void prepareHql(StringBuilder hql, Searchable searchable) {
		int pos = 0;
		boolean isFirst = true;
		for (SearchFilter filter : searchable.getSearchFilters()) {
			if (isFirst) {
				pos = genFilter(hql, filter, pos);
				isFirst = false;
			} else {
				hql.append(" " + filter.getType().getTypeStr() + " ");
				pos = genFilter(hql, filter, pos);
			}
		}
	}

	/**
	 * <p>
	 * 预处理HQL语句
	 * </p>
	 * 根据基本hql生成包含预处理排序参数的hql语句
	 * 
	 * @param hql
	 * @param sort
	 */
	protected void prepareOrder(StringBuilder hql, Sort sort) {
		if (sort != null && sort.isNotEmpty()) {
			hql.append(" order by ");
			for (Sort.Order order : sort) {
				hql.append(String.format("e.%s %s, ", order.getProperty(), order.getDirection().name()));
			}
			hql.deleteCharAt(hql.length() - 2);
		}
	}

	/**
	 * <p>
	 * 预处理HQL语句
	 * </p>
	 * 给包含预处理参数的query赋值
	 * 
	 * @param query
	 * @param searchable
	 */
	protected void prepareValues(Query query, Searchable searchable) {
		int pos = 0;
		for (SearchFilter filter : searchable.getSearchFilters()) {
			pos = setValue(query, filter, pos);
		}
	}

	/**
	 * <p>
	 * 处理HQL语句
	 * </p>
	 * query中的预处理参数赋值
	 * 
	 * @param query
	 * @param filter
	 * @param pos
	 */
	protected int setValue(Query query, SearchFilter filter, int pos) {
		if (((Condition) filter).isInFilter()) { // 先判定是不是in操作
			if ((filter.getValue() instanceof Object[])) {
				query.setParameterList(filter.getProperty() + StringPool.UNDERLINE + pos, (Object[]) filter.getValue());
			} else if ((filter.getValue() instanceof Collection)) {
				query.setParameterList(filter.getProperty() + StringPool.UNDERLINE + pos,
						(Collection) filter.getValue());
			} else {
				query.setParameter(filter.getProperty() + StringPool.UNDERLINE + pos,
						((Condition) filter).getFormatValues());
			}
			pos++;
		} else if (((Condition) filter).isUnaryFilter()) { // 再判定是不是一元操作
			// 一元操作符后不跟参数
		} else {
			query.setParameter(filter.getProperty() + StringPool.UNDERLINE + pos,
					((Condition) filter).getFormatValues());
			pos++;
		}

		if (filter.hasChildFilter()) {
			for (SearchFilter childFilter : filter.getChildFilters()) {
				pos = setValue(query, childFilter, pos);
			}
		}
		return pos;
	}

	/**
	 * <p>
	 * 处理HQL语句
	 * </p>
	 * 根据基本hql和查询条件生成包含预处理查询参数的hql语句
	 * 
	 * @param hql
	 * @param filter
	 * @param pos
	 * @return
	 */
	protected int genFilter(StringBuilder hql, SearchFilter filter, int pos) {
		boolean hasChildFilter = filter.hasChildFilter();

		if (hasChildFilter) {
			hql.append("(");
		}

		hql.append("e.").append(filter.getProperty()).append(" ").append(filter.getOperator().getSymbol());

		if (((Condition) filter).isInFilter()) { // 先判定是不是in操作
			hql.append("(").append(" :").append(filter.getProperty()).append(StringPool.UNDERLINE).append(pos)
					.append(")");
			pos++;
		} else if (((Condition) filter).isUnaryFilter()) { // 再判定是不是一元操作
			// 一元操作符后不跟参数
		} else {
			hql.append(" :").append(filter.getProperty()).append(StringPool.UNDERLINE).append(pos);
			pos++;
		}

		if (hasChildFilter) {
			for (SearchFilter childFilter : filter.getChildFilters()) {
				hql.append(" " + childFilter.getType().getTypeStr() + " ");
				pos = genFilter(hql, childFilter, pos);
			}
			hql.append(")");
		}
		return pos;
	}

	/**
	 * 应用查询缓存
	 * 
	 * @param query
	 */
	protected void enableQueryCache(Query query) {
		if (enabledQueryCache) {
			query.setCacheable(true);
		}
	}

	/**
	 * 应用查询缓存
	 * 
	 * @param criteria
	 */
	protected void enableQueryCache(Criteria criteria) {
		if (enabledQueryCache) {
			criteria.setCacheable(true);
		}
	}

}
