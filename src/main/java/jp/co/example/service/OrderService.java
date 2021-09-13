package jp.co.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.Item;
import jp.co.example.domain.Order;
import jp.co.example.domain.OrderItem;
import jp.co.example.domain.OrderTopping;
import jp.co.example.domain.Status;
import jp.co.example.domain.Topping;
import jp.co.example.form.AddCartForm;
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

	/**
	 * ショッピングカートに商品を追加します.
	 * @param userId ユーザーId
	 * @param form ユーザーの入力データ
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
		if (form.getToppingIdList().size() != 0) {
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
	 * @param userId ユーザーId
	 * @return　注文情報
	 */
	public Order showShoppingCart(Integer userId) {
		int status = Status.BEFORE_ORDER.getKey();
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		for(OrderItem orderItem:order.getOrderItemList()) {
			System.out.println(orderItem);
		}
		return order;
	}
	
}
