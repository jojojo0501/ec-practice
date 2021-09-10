package jp.co.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.User;
import jp.co.example.form.LoginForm;
import jp.co.example.form.RegisterUserForm;
import jp.co.example.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public RegisterUserForm setUpRegisterForm() {
		return new RegisterUserForm();
	}
	
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/**
	 * ユーザー登録画面へ遷移します.
	 * 
	 * @return ユーザー登録画面へフォワードします
	 */
	@RequestMapping("/register")
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

	/**
	 * ログイン処理を行います.
	 * @param form 入力された情報
	 * @param model リクエストスコープにエラーメッセージを格納します.
	 * @return 商品一覧画面を表示
	 */
	@RequestMapping("/login")
	public String login(LoginForm form, Model model) {
		User user = userService.searchByEmailAndPassword(form.getEmail(), form.getPassword());
		if (user == null) {
			model.addAttribute("loginErrorMessage", "メールアドレスまたはパスワードが間違っています。");
			return "login";
		} else {
			session.setAttribute("user", user);
			return "redirect:/";
		}
	}
	
	/**
	 * ログアウト処理を行います.
	 * @return 商品一覧画面へフォワードします
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}
}
