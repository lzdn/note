package com.lzdn.note.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.lzdn.note.common.dao.BaseDao;
import com.lzdn.note.entity.User;
import com.lzdn.note.util.StringUtil;

@Repository
public class UserDao extends BaseDao<User> {

	/**
	 * 根据用户主键删除用户角色关系
	 * 
	 * @param id
	 *            用户ID
	 */
	public void deleteUserRoleById(Long id) {
		Query query = getSession().createSQLQuery("delete from user_role where user_id = :userId");
		query.setParameter("userId", id);
		query.executeUpdate();
	}

	/**
	 * 部分更新用户表字段
	 * 
	 * @param user
	 *            待更新实体
	 */
	public void mergeUser(User user) {
		StringBuilder sb = new StringBuilder();
		sb.append("update " + User.class.getName() + " e set ");

		String[] props = new String[5];
		Object[] values = new Object[5];

		int pos = 0;
		// 拼接需要更新的字段属性名
		if (StringUtil.isNotEmpty(user.getUserName())) {
			props[pos] = "userName";
			values[pos] = user.getUserName();
			sb.append(props[pos] + " = :v_" + props[pos] + ",");
			pos++;
		}
		if (StringUtil.isNotEmpty(user.getPassword())) {
			props[pos] = "password";
			values[pos] = user.getPassword();
			sb.append(props[pos] + " = :v_" + props[pos] + ",");
			pos++;
		}

		if (user.getStatus() != null) {
			props[pos] = "status";
			values[pos] = user.getStatus();
			sb.append(props[pos] + " = :v_" + props[pos] + ",");
		}

		// 拼接固定语句
		// sb.append("modifyTime = now() where id = " + user.getUserId());
		sb.append(" where id = " + user.getUserId());

		Query query = getSession().createQuery(sb.toString());
		// 拼接需要更新的字段属性值
		for (int i = 0; i <= pos; i++) {
			query.setParameter("v_" + props[i], values[i]);
		}
		query.executeUpdate();
	}

}
