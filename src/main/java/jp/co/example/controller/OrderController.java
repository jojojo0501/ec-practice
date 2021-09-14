package jp.co.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.Order;
import jp.co.example.domain.User;
import jp.co.example.form.AddCartForm;
import jp.co.example.form.OrderForm;
import jp.co.example.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderService;

	@ModelAttribute
	public AddCartForm setUpAddCartForm() {
		return new AddCartForm();
	}
	
	@ModelAttribute
	public OrderForm setUpOrderForm() {
		return new OrderForm();
	}

	/**
	 * ショッピングカート内の商品を表示します.
	 * @return
	 */
	@RequestMapping("/showCart")
	public String showShoppingCart() {
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		Order order = orderService.showShoppingCart(userId);
		session.setAttribute("order",order);
		return "cart_list";
	}

	/**
	 * ショッピングカート内に商品を追加します.
	 * @param form 入力データ
	 * @return　ショッピングカート一覧画面へリダイレクトする.
	 */
	@RequestMapping("/addCart")
	public String addShoppingCart(AddCartForm form) {
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		orderService.addShoppingCart(userId, form);
		return "redirect:/order/showCart";
	}
	
	/**
	 * ショッピングカート内の商品を削除します.
	 * @param orderItemId 削除する商品id
	 * @return ショッピングカート一覧画面へリダイレクトする.
	 */
	@RequestMapping("/deleteCart")
	public String deleteOrderItemsAndOrderToppings(Integer orderItemId,Integer orderId) {
		orderService.deleteOrderItemAndOrderTopping(orderItemId,orderId);
		return "redirect:/order/showCart";
	}
	
	/**
	 * 注文確認画面へ遷移します.
	 * @return 注文確認画面へフォワードする。
	 */
	@RequestMapping("/toOrderConfirm")
	public String toOrderConfirm() {
		return "order_confirm";
	}
	
	/**
	 * 注文する.
	 * @param form 入力フォーム情報
	 * @param orderId　注文Id
	 * @return 注文完了画面へリダイレクト
	 */
	@RequestMapping("/orderResult")
	public String order(OrderForm form,Integer orderId) {
		orderService.updateOrder(form, orderId);
		return "redirect:/order/toOrderFinished";
	}
	
	/**
	 * 注文確認画面へ遷移します.
	 * @return 注文確認画面へフォワードします。
	 */
	@RequestMapping("/toOrderFinished")
	public String toOrderFinished() {
		return "order_finished";
	}
	

}
