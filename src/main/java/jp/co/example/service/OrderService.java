package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.Order;
import jp.co.example.domain.OrderItem;
import jp.co.example.domain.OrderTopping;
import jp.co.example.domain.Status;
import jp.co.example.domain.Topping;
import jp.co.example.form.AddCartForm;
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
	private ToppingRepository toppingRepository;

	public void addShoppingCart(Integer userId, AddCartForm form) {
		int status = Status.BEFORE_ORDER.getKey();
		int defaultTotalPrice = 0;
		Order order = orderRepository.findByUserIdAndStatus(userId, status);
		if (order.getId() == null) {
			order = new Order();
			order.setUserId(userId);
			order.setStatus(status);
			order.setTotalPrice(defaultTotalPrice);
			order = orderRepository.insert(order);
		}
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(form.getIntItemId());
		orderItem.setOrderId(order.getId());
		orderItem.setQuantity(form.getIntQuantity());
		orderItem.setSize(form.getCharSize());
		orderItemRepository.insert(orderItem);
		if (form.getToppingIdList().size() != 0) {
			for (String toppingId : form.getToppingIdList()) {
				Integer intToppingId = Integer.parseInt(toppingId);
				Topping topping = toppingRepository.load(intToppingId);
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(topping.getId());
				orderTopping.setOrderItemId(orderItem.getId());
				orderToppingRepository.insert(orderTopping);
			}
		}
	}
}
