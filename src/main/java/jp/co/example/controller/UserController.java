package jp.co.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.User;
import jp.co.example.form.RegisterUserForm;
import jp.co.example.service.UserService;

@Controller
@RequestMapping("/register")
public class UserController {

	@Autowired
	private UserService userService;

	@ModelAttribute
	public RegisterUserForm setUpForm() {
		return new RegisterUserForm();
	}

	/**
	 * ユーザー登録画面へ遷移します.
	 * 
	 * @return ユーザー登録画面へフォワードします
	 */
	@RequestMapping("")
	public String index() {
		return "register_user";
	}

	/**
	 * ユーザー情報を登録します.
	 * 
	 * @return ログイン画面へリダイレクトします。
	 */
	@RequestMapping("/insert")
	public String registerUser(@Validated RegisterUserForm form, BindingResult result) {
		// パスワード確認
		if (!(form.getPassword().equals(form.getConfirmPassword()))) {
			result.rejectValue("confirmPassword", "", "パスワードが一致していません。");
		}
		// メールアドレス重複チェック
		User duplicateUser = userService.findByEmail(form.getEmail());
		if ((duplicateUser != null)) {
			result.rejectValue("email", "", "そのメールアドレスは既に登録されています。");
		}

		if (result.hasErrors()) {
			return index();
		}
		User user = new User();
		// フォームの値をコピー
		BeanUtils.copyProperties(form, user);
		System.out.println(user);
		userService.insert(user);
		return "redirect:/register/toLogin";
	}

	/**
	 * ログイン画面へ遷移する.
	 * 
	 * @return ログイン画面へフォワードします.
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}
}
