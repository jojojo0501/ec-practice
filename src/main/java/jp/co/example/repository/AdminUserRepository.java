package jp.co.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.domain.AdminUser;

@Repository
public class AdminUserRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * AdminUserオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<AdminUser> ADMIN_USER_ROW_MAPPER = new BeanPropertyRowMapper<>(AdminUser.class);

	/**
	 * 管理者を登録します.
	 * 
	 * @param adminUser 登録する管理者情報
	 */
	public void insert(AdminUser adminUser) {
		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO admin_users(name,email,password)");
		insertSql.append(" VALUES(:name,:email,:password);");
		SqlParameterSource param = new BeanPropertySqlParameterSource(adminUser);
		template.update(insertSql.toString(), param);
	}

	/**
	 * メールアドレスをもとに管理者を検索します.
	 * 
	 * @param email メールアドレス
	 * @return 管理者情報
	 */
	public AdminUser findByEmail(String email) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,email,password from admin_users WHERE email=:email;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<AdminUser> adminUserList = template.query(sql.toString(), param, ADMIN_USER_ROW_MAPPER);
		if (adminUserList.size() == 0) {
			return null;
		}
		return adminUserList.get(0);
	};

}
