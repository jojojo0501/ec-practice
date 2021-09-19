package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.User;
import jp.co.example.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 主キーをもとにユーザー情報を取得します.
	 * 
	 * @param userId ユーザーId
	 * @return ユーザー情報
	 */
	public User load(Integer userId) {
		return userRepository.load(userId);
	}

	/**
	 * ユーザー情報を登録します.
	 * 
	 * @param user ユーザー情報
	 */
	public void insert(User user) {
		// パスワードをハッシュ化
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.insert(user);
	}

	/**
	 * メールアドレスをもとのユーザ情報を検索します.
	 * 
	 * @param email メールアドレス
	 * @return ユーザー情報
	 */
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * メールアドレスとパスワードよりユーザーを検索します.
	 * 
	 * @param email    メールアドレス
	 * @param password パスワード
	 * @return ユーザー情報
	 */
	public User login(String email, String password) {
		User user = userRepository.findByEmail(email);
		//ユーザーが見つからない場合nullを返す
		if(user == null) {
			return null;
		}
		//パスワードが不一致の場合はnullを返す
		if(!passwordEncoder.matches(password, user.getPassword())) {
			return null;
		}
		return user;
	}
	
	/**
	 * ユーザー情報を更新します.
	 * @param user 更新するユーザー情報
	 * @return 更新後のユーザー情報
	 */
	public User updateUser(User user) {
		return userRepository.update(user);
	}
}
