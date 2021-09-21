package jp.co.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.AdminUser;
import jp.co.example.form.LoginAdminUserForm;
import jp.co.example.form.RegisterAdminUserForm;
import jp.co.example.service.AdminUserService;

/**
 * 管理者を操作するコントローラークラス.
 * 
 * @author kanekojota
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public RegisterAdminUserForm setUpRegisterForm() {
		return new RegisterAdminUserForm();
	}

	@ModelAttribute
	public LoginAdminUserForm setUpLoginForm() {
		return new LoginAdminUserForm();
	}

	/**
	 * 管理者登録画面へ遷移します.
	 * 
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

	/**
	 * 管理者ログイン処理を行うます.
	 * 
	 * @param form  入力情報
	 * @param model リクエストスコープにメッセージを格納します。
	 * @return 管理者商品一覧ページへリダイレクトします.
	 */
	@RequestMapping("/login")
	public String login(LoginAdminUserForm form, Model model) {
		AdminUser adminUser = adminUserService.findByEmail(form.getEmail());
		if (adminUser == null) {
			model.addAttribute("loginErrorMessage", "メールアドレスまたはパスワードが間違っています。");
			return "admin_login";
		} else if (!passwordEncoder.matches(form.getPassword(), adminUser.getPassword())) {
			model.addAttribute("loginErrorMessage", "メールアドレスまたはパスワードが間違っています。");
			return "admin_login";
		} else {
			session.setAttribute("adminUser", adminUser);
			return "redirect:/admin";
		}
	}

	/**
	 * 管理者用商品一覧ページへ遷移します.
	 * 
	 * @return 管理者一覧ページへフォワードします。
	 */
	@RequestMapping("")
	public String index() {
		return "admin_item_list_pizza";
	}

}
