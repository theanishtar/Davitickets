package com.davisys.reponsitory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.davisys.entity.Roles;
import com.davisys.entity.Users;

@Repository
public class RoleCustomRepo {
	@PersistenceContext
	private EntityManager entityManager;

	public List<Roles> getRole(Users user) {
		StringBuilder sql = new StringBuilder()
				.append("SELECT r.name, r.role_des as name FROM users u INNER JOIN user_role ur ON u.userid = ur.userid \r\n"
						+ "INNER JOIN roles r ON r.role_id =ur.role_id ");
		sql.append("WHERE 1=1 ");
		if (user.getEmail() != null) {
			sql.append(" and email=:email");
		}
		NativeQuery<Roles> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
		if (user.getEmail() != null) {
			query.setParameter("email", user.getEmail());
		}
		query.addScalar("name", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(Roles.class));
		return query.list();
	}
}
