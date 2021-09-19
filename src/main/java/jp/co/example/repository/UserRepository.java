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

import jp.co.example.domain.User;

/**
 * Userテーブルを操作するリポジトリ.
 * 
 * @author kanekojota
 *
 */
@Repository
public class UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Userオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

	/**
	 * ユーザー情報を主キー検索します.
	 * 
	 * @param id ユーザーId
	 * @return
	 */
	public User load(Integer id) {
		String sql = "SELECT id,name, email, password, zipcode, address, telephone FROM users where id=:id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}

	/**
	 * ユーザー登録をする.
	 * 
	 * @param user ユーザー情報
	 * @return 入力するユーザー情報
	 */
	public User insert(User user) {
		StringBuilder insertSql = new StringBuilder();
		insertSql.append(
				"INSERT INTO users(name,email,password,zipcode,address,telephone) values(:name,:email,:password,:zipcode,:address,:telephone);");
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(insertSql.toString(), param);
		return user;
	}

	/**
	 * メールアドレスをもとにユーザーを検索します.
	 * 
	 * @param email メールアドレス
	 * @return ユーザー情報
	 */
	public User findByEmail(String email) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,email,password,zipcode,address,telephone from users WHERE email=:email;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<User> userList = template.query(sql.toString(), param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	};

	/**
	 * メールアドレスとパスワードよりユーザーを検索します.
	 * 
	 * @param email    メールアドレス
	 * @param password パスワード
	 * @return ユーザー情報
	 */
	public User findByEmailAndPassword(String email, String password) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT id,name,email,password,zipcode,address,telephone from users WHERE email=:email AND password=:password;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		List<User> userList = template.query(sql.toString(), param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	};
}
