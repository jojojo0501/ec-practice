package jp.co.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.User;
import jp.co.example.form.AddCartForm;
import jp.co.example.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderService;

	@ModelAttribute
	public AddCartForm setUpForm() {
		return new AddCartForm();
	}

	@RequestMapping("/showCart")
	public String showShoppingCart() {
		return "cart_list";
	}

	@RequestMapping("/addCart")
	public String addShoppingCart(AddCartForm form) {
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		orderService.addShoppingCart(userId, form);
		return "redirect:/order/showCart";
	}

}
