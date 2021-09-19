package jp.co.example.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.CreditCardPaymenResponse;
import jp.co.example.domain.Order;
import jp.co.example.domain.User;
import jp.co.example.form.AddCartForm;
import jp.co.example.form.OrderForm;
import jp.co.example.service.CreditCardPaymentService;
import jp.co.example.service.OrderService;
import jp.co.example.service.UserService;

@Controller
@EnableAsync
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private CreditCardPaymentService creditCardPaymentSerice;

	@ModelAttribute
	public AddCartForm setUpAddCartForm() {
		return new AddCartForm();
	}

	@ModelAttribute
	public OrderForm setUpOrderForm() {
		OrderForm orderForm = new OrderForm();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			Integer userId = (Integer) session.getAttribute("noLoginUSerId");
			if (userId == null) {
				userId = createRandomUserId();
				session.setAttribute("noLoginUSerId", userId);
			}
		}
		if (user != null) {
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail(user.getEmail());
			orderForm.setDestinationZipcode(user.getZipcode());
			orderForm.setDestinationAddress(user.getAddress());
			orderForm.setDestinationTel(user.getTelephone());
		}
		return orderForm;
	}

	/**
	 * ショッピングカート内の商品を表示します.
	 * 
	 * @return
	 */
	@RequestMapping("/showCart")
	public String showShoppingCart(Model model) {
		User user = (User) session.getAttribute("user");
		Integer userId = 0;
		if (user == null) {
			userId = (Integer) session.getAttribute("noLoginUSerId");
			if (userId == null) {
				userId = createRandomUserId();
				session.setAttribute("noLoginUSerId", userId);
			}
		} else {
			userId = user.getId();
		}
		Order order = orderService.showShoppingCart(userId);
		if (order.getId() == null || order.getOrderItemList().size() == 0) {
			model.addAttribute("noItemMessage", "ショッピングカートに商品がありません。");
		}
		session.setAttribute("order", order);
		return "cart_list";
	}

	/**
	 * ショッピングカート内に商品を追加します.
	 * 
	 * @param form 入力データ
	 * @return ショッピングカート一覧画面へリダイレクトする.
	 */
	@RequestMapping("/addCart")
	public String addShoppingCart(AddCartForm form) {
		User user = (User) session.getAttribute("user");
		Integer userId = 0;
		if (user == null) {
			userId = (Integer) session.getAttribute("noLoginUSerId");
			if (userId == null) {
				userId = createRandomUserId();
				session.setAttribute("noLoginUSerId", userId);
			}
		} else {
			userId = user.getId();
		}
		System.out.println("userId:" + userId);
		orderService.addShoppingCart(userId, form);
		return "redirect:/order/showCart";
	}

	/**
	 * ランダムなユーザーIDを生成します.
	 * 
	 * @return ユーザーID
	 */
	public Integer createRandomUserId() {
		Random rnd = new Random();
		Integer randomUserId = 0;
		for (; true;) {
			randomUserId = rnd.nextInt();
			User existUser = userService.load(randomUserId);
			if (existUser == null) {
				break;
			}
		}
		return randomUserId;
	}

	/**
	 * ショッピングカート内の商品を削除します.
	 * 
	 * @param orderItemId 削除する商品id
	 * @return ショッピングカート一覧画面へリダイレクトする.
	 */
	@RequestMapping("/deleteCart")
	public String deleteOrderItemsAndOrderToppings(Integer orderItemId, Integer orderId) {
		orderService.deleteOrderItemAndOrderTopping(orderItemId, orderId);
		return "redirect:/order/showCart";
	}

	/**
	 * 注文確認画面へ遷移します.
	 * 
	 * @return 注文確認画面へフォワードする。
	 */
	@RequestMapping("/toOrderConfirm")
	public String toOrderConfirm() {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/toLogin";
		}
		return "order_confirm";
	}

	/**
	 * 注文する.
	 * 
	 * @param form    入力フォーム情報
	 * @param orderId 注文Id
	 * @return 注文完了画面へリダイレクト
	 */
	@RequestMapping("/orderResult")
	public String order(@Validated OrderForm form, BindingResult result, Model model) {
		Boolean orderResult = false;
		if ("2".equals(form.getPaymentMethod())) {
			// クレジットカード支払い処理
			CreditCardPaymenResponse creditCardPaymenResponse = creditCardPaymentSerice.paymentApiService(form);
			System.out.println(creditCardPaymenResponse);
			if ("success".equals(creditCardPaymenResponse.getStatus())) {
				orderResult = orderService.updateOrder(form);
				if (result.hasErrors() || !orderResult) {
					model.addAttribute("deliveryTimeMessage", "今から３時間後以降の日時をご入力ください。");
					return toOrderConfirm();
				}
				return "redirect:/order/toOrderFinished";
			} else {
				model.addAttribute("creditCardErrorMessage", "クレジットカード情報が不正です");
				return toOrderConfirm();
			}
		}
		orderResult = orderService.updateOrder(form);
		if (result.hasErrors() || !orderResult) {
			model.addAttribute("deliveryTimeMessage", "今から３時間後以降の日時をご入力ください。");
			return toOrderConfirm();
		}
		if ("1".equals(form.getPaymentMethod())) {
			return "redirect:/order/toOrderFinished";
		}
		return toOrderConfirm();
	}

	/**
	 * 注文完了画面へ遷移します.
	 * 
	 * @return 注文完了画面へフォワードします。
	 */
	@RequestMapping("/toOrderFinished")
	public String toOrderFinished() {
		return "order_finished";
	}

	/**
	 * 注文履歴画面へ遷移します.
	 * 
	 * @return 注文履歴画面へフォワード
	 */
	@RequestMapping("/toOrderhistory")
	public String toOrderHistory(Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/toLogin";
		}
		List<Order> orderList = orderService.showOrderHistory();
		if (orderList.size() == 0) {
			model.addAttribute("noOrderMessage", "過去に注文した商品がありません。");
		}
		model.addAttribute("orderList", orderList);
		return "order_history";
	}

}
