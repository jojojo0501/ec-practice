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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.example.domain.User;
import jp.co.example.form.LoginForm;
import jp.co.example.form.RegisterUserForm;
import jp.co.example.form.UpdateUserForm;
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

	@ModelAttribute
	public UpdateUserForm setUpUpdateUserForm() {
		UpdateUserForm updateUserForm = new UpdateUserForm();
		User user = (User) session.getAttribute("user");
		if (user != null) {
			updateUserForm.setName(user.getName());
			updateUserForm.setEmail(user.getEmail());
			updateUserForm.setAddress(user.getAddress());
			updateUserForm.setTelephone(user.getTelephone());
			updateUserForm.setZipcode(user.getZipcode());
		}
		return updateUserForm;
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
		userService.insert(user);
		return "redirect:/user/toLogin";
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
	 * 
	 * @param form  入力された情報
	 * @param model リクエストスコープにエラーメッセージを格納します.
	 * @return 商品一覧画面を表示
	 */
	@RequestMapping("/login")
	public String login(LoginForm form, Model model) {
		User user = userService.login(form.getEmail(), form.getPassword());
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
	 * 
	 * @return 商品一覧画面へフォワードします
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}

	/**
	 * ユーザー情報更新画面へ遷移します.
	 * 
	 * @return ユーザー情報更新画面へフォワードします。
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate() {
		return "update_user";
	}

	/**
	 * ユーザー情報を更新します.
	 * 
	 * @param form  入力データ
	 * @param model リクエストスコープに更新完了メッセージを格納します。
	 * @return ユーザー更新画面へリダイレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateUserForm form, BindingResult result,RedirectAttributes redirectAttribute) {
		// パスワード確認
		if (!(form.getPassword().equals(form.getConfirmPassword()))) {
			result.rejectValue("confirmPassword", "", "パスワードが一致していません。");
		}
		// メールアドレス重複チェック
		User loginUser = (User) session.getAttribute("user");
		if (!(loginUser.getEmail().equals(form.getEmail()))) {
			User duplicateUser = userService.findByEmail(form.getEmail());
			if ((duplicateUser != null)) {
				result.rejectValue("email", "", "そのメールアドレスは既に登録されています。");
			}
		}
		if (result.hasErrors()) {
			return toUpdate();
		}
		User user = (User) session.getAttribute("user");
		BeanUtils.copyProperties(form, user);
		userService.updateUser(user);
		redirectAttribute.addFlashAttribute("updateMessage", "ユーザー情報を更新しました！");
		return "redirect:/user/toUpdate";
	}
}
