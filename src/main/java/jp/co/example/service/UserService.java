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
}
