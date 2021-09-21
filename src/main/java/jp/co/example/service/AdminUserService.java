package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.AdminUser;
import jp.co.example.repository.AdminUserRepository;

/**
 * 管理者情報を扱うサービスクラス.
 * 
 * @author kanekojota
 *
 */
@Service
@Transactional
public class AdminUserService {

	@Autowired
	private AdminUserRepository adminUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 管理者を登録します.
	 * 
	 * @param adminUser 管理者情報
	 */
	public void registerAdminUser(AdminUser adminUser) {
		adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
		adminUserRepository.insert(adminUser);
	}

	/**
	 * メールアドレスをもとに管理者情報を検索します.
	 * 
	 * @param email メールアドレス
	 * @return 管理者情報
	 */
	public AdminUser findByEmail(String email) {
		return adminUserRepository.findByEmail(email);
	}

}
