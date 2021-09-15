package jp.co.example.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.Item;
import jp.co.example.domain.Order;
import jp.co.example.domain.OrderItem;
import jp.co.example.domain.OrderTopping;
import jp.co.example.domain.PaymentMethod;
import jp.co.example.domain.Status;
import jp.co.example.domain.Topping;
import jp.co.example.domain.User;
import jp.co.example.form.AddCartForm;
import jp.co.example.form.OrderForm;
import jp.co.example.repository.ItemRepository;
import jp.co.example.repository.OrderItemRepository;
import jp.co.example.repository.OrderRepository;
import jp.co.example.repository.OrderToppingRepository;
import jp.co.example.repository.ToppingRepository;

@Service
@Transactional
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderToppingRepository orderToppingRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ToppingRepository toppingRepository;

	@Autowired
	private HttpSession session;

	/**
	 * ショッピングカートに商品を追加します.
	 * 
	 * @param userId ユーザーId
	 * @param form   ユーザーの入力データ
	 */
	public void addShoppingCart(Integer userId, AddCartForm form) {
		int status = Status.BEFORE_ORDER.getKey();
		int defaultTotalPrice = 0;
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		if (order.getId() == null) {
			order = new Order();
			order.setUserId(userId);
			order.setStatus(status);
			order.setTotalPrice(defaultTotalPrice);
			order.setOrderItemList(new ArrayList<>());
			order = orderRepository.insert(order);
		}
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(form.getIntItemId());
		Item item = itemRepository.load(form.getIntItemId());
		orderItem.setItem(item);
		orderItem.setOrderId(order.getId());
		orderItem.setQuantity(form.getIntQuantity());
		orderItem.setSize(form.getCharSize());
		orderItemRepository.insert(orderItem);
		List<OrderTopping> orderToppingList = new ArrayList<>();
		if (form.getToppingIdList() != null) {
			for (String toppingId : form.getToppingIdList()) {
				Integer intToppingId = Integer.parseInt(toppingId);
				Topping topping = toppingRepository.load(intToppingId);
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(topping.getId());
				orderTopping.setTopping(topping);
				orderTopping.setOrderItemId(orderItem.getId());
				orderTopping = orderToppingRepository.insert(orderTopping);
				orderToppingList.add(orderTopping);
			}
		}
		// 以下で注文の合計金額を更新
		orderItem.setOrderToppingList(orderToppingList);
		order.getOrderItemList().add(orderItem);
		orderRepository.updateTotalPrice(order.getId(), order.getCalcSubTotalPrice());
	}

	/**
	 * ショッピングカート内の注文情報を取得します.
	 * 
	 * @param userId ユーザーId
	 * @return 注文情報
	 */
	public Order showShoppingCart(Integer userId) {
		int status = Status.BEFORE_ORDER.getKey();
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		return order;
	}

	/**
	 * ショッピングカート内の商品を削除する.
	 * 
	 * @param orderItemId 削除する商品のid
	 */
	public void deleteOrderItemAndOrderTopping(int orderItemId, int orderId) {
		orderItemRepository.deleteOrderItemAndOrderTopping(orderItemId);
		// ショッピングカート内の商品を削除したため、注文情報の金額へ反映する
		int status = Status.BEFORE_ORDER.getKey();
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		orderRepository.updateTotalPrice(order.getId(), order.getCalcSubTotalPrice());
	}

	/**
	 * 注文情報を更新します.
	 * 
	 * @param form 入力データ
	 * @return 希望配達日時が現在時刻から3時間未満の場合はfalseを返す。それ以外はtrueを返す。
	 */
	public Boolean updateOrder(OrderForm form) {
		Order order = orderRepository.load(form.getIntOrderId());
		// フォームの値をオブジェクトにコピーする。
		BeanUtils.copyProperties(form, order);
		// コピーされていないものを手動でコピーする。
		// 注文日の作成(現在時刻をDate型で取得する。)
		Date orderDate = new Date();
		order.setOrderDate(orderDate);
		order.setPaymentMethod(form.getIntPaymentMethod());
		// formで受け取ったデータより配達日時をString型で作成
		String stringDeliveryTime = form.getDeliveryDate() + "-" + form.getDeliveryTime() + "-00-00";
		try {
			// String型の配達日時をDate型に変換する。
			Date DatedeliveryTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(stringDeliveryTime);
			long limitHour = 10800000;
			if (DatedeliveryTime.getTime() - orderDate.getTime() < limitHour) {
				return false;
			}
			// Date型の配達日時をTimestamp型へ変換する。
			Timestamp deliveryTime = new Timestamp(DatedeliveryTime.getTime());
			order.setDeliveryTime(deliveryTime);
			//以下でステータスの更新を行う。
			if (PaymentMethod.CASH_ON_DELIVERY.getKey()==form.getIntPaymentMethod()) {
				order.setStatus(Status.NOT_PAYMENT.getKey());
			} else if (PaymentMethod.CREDIT_CARD.getKey()==form.getIntPaymentMethod()) {
				order.setStatus(Status.DEPOSITED.getKey());
			}
			orderRepository.update(order);
		} catch (java.text.ParseException pe) {
			pe.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 注文履歴情報を取得します.
	 * @return 注文履歴情報
	 */
	public List<Order> showOrderHistory(){
		int status = Status.BEFORE_ORDER.getKey();
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		List<Order> orderList = orderRepository.findByUserIdAndStatusNotZero(userId, status);
		return orderList;
	}

}
