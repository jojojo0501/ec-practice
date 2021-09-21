package jp.co.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.AdminUser;
import jp.co.example.form.RegisterAdminUserForm;
import jp.co.example.service.AdminUserService;

/**
 * 管理者を操作するコントローラークラス.
 * @author kanekojota
 *
 */
@RequestMapping("/admin")
public class AdminUserController {
	
	@Autowired
	private AdminUserService adminUserService;
	
	@ModelAttribute
	public RegisterAdminUserForm setUpRegisterForm() {
		return new RegisterAdminUserForm();
	}
	
	/**
	 * 管理者登録画面へ遷移します.
	 * @return 管理者登録画面へフォワードします。
	 */
	@RequestMapping("/register")
	public String register() {
		return "register_admin_user";
	}
	
	/**
	 * 管理者情報を登録します.
	 * 
	 * @return 管理者ログイン画面へリダイレクトします。
	 */
	@RequestMapping("/insert")
	public String registerAdminUser(@Validated RegisterAdminUserForm form, BindingResult result) {
		// パスワード確認
		if (!(form.getPassword().equals(form.getConfirmPassword()))) {
			result.rejectValue("confirmPassword", "", "パスワードが一致していません。");
		}
		// メールアドレス重複チェック
		AdminUser duplicateUser = adminUserService.findByEmail(form.getEmail());
		if ((duplicateUser != null)) {
			result.rejectValue("email", "", "そのメールアドレスは既に登録されています。");
		}

		if (result.hasErrors()) {
			return register();
		}
		AdminUser adminUser = new AdminUser();
		// フォームの値をコピー
		BeanUtils.copyProperties(form, adminUser);
		adminUserService.registerAdminUser(adminUser);
		return "redirect:/admin/toLogin";
	}
	
	/**
	 * 管理者ログイン画面へ遷移する.
	 * 
	 * @return 管理者ログイン画面へフォワードします.
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "admin_login";
	}

}
