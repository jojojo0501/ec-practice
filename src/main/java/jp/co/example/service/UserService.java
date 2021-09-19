package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.User;
import jp.co.example.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 主キーをもとにユーザー情報を取得します.
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
	 * @param email　メールアドレス
	 * @param password パスワード
	 * @return ユーザー情報
	 */
	public User searchByEmailAndPassword(String email,String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}
}
