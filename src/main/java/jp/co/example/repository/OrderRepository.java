package jp.co.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jp.co.example.domain.Item;
import jp.co.example.domain.Order;
import jp.co.example.domain.OrderItem;
import jp.co.example.domain.OrderTopping;
import jp.co.example.domain.Topping;

@Repository
public class OrderRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Orderオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Order> ORDER_ROW_MAPPER = new BeanPropertyRowMapper<>(Order.class);

	private static final ResultSetExtractor<Order> ORDER_RESULT_SET_EXTRACTOR = (rs) -> {
		// Orderオブジェクトの作成
		Order order = new Order();
		// オブジェクトを格納するリストの作成
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		List<Topping> toppingList = null;
		// orderオブジェクトに格納するオブジェクトの作成（レコードに各情報が見つかったときにインスタンス化するため、初期値はnullとする。）
		OrderItem orderItem = null;
		Item item = null;
		OrderTopping orderTopping = null;
		Topping topping = null;
		// 前の行と比較するため、各idを初期変数として定義
		int beforeOrderId = 0;
		int beforeOrderItemId = 0;
		int beforeOrderToppingId = 0;
		int beforeItemId = 0;
		int beforeToppingId = 0;

		while (rs.next()) {
			// 現在件sなくされているIdを取得
			int nowOrderId = rs.getInt("o_id");
			int nowOrderItemId = rs.getInt("i_id");
			int nowOrderToppingId = rs.getInt("ot_topping_id");
			int nowItemId = rs.getInt("oi_item_id");
			int nowToppingId = rs.getInt("t_id");

			if (nowOrderId != beforeOrderId) {
				order.setId(rs.getInt("o_id"));
				order.setUserId(rs.getInt("o_user_id"));
				order.setStatus(rs.getInt("o_status"));
				order.setTotalPrice(rs.getInt("o_total_price"));
				order.setOrderDate(rs.getDate("o_order_date"));
				order.setDestinationName(rs.getString("o_destination_name"));
				order.setDestinationEmail(rs.getString("o_destination_email"));
				order.setDestinationZipcode(rs.getString("o_destination_zipcode"));
				order.setDestinationAddress(rs.getString("o_destination_address"));
				order.setDestinationTel(rs.getString("o_destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("o_delivery_time"));
				order.setPaymentMethod(rs.getInt("o_payment_method"));
				orderItemList = new ArrayList<>();
				order.setOrderItemList(orderItemList);
			}

			if (rs.getInt("oi_id") != 0) {
				if (nowOrderItemId != beforeOrderItemId) {
					orderItem = new OrderItem();
					orderItem.setId(rs.getInt("oi_id"));
					orderItem.setItemId(rs.getInt("oi_item_id"));
					orderItem.setOrderId(rs.getInt("oi_order_id"));
					orderItem.setQuantity(rs.getInt("oi_quantity"));
					orderItem.setSizeStringFromChar(rs.getString("oi_size"));
					orderItem.setItem(item);
					orderToppingList = new ArrayList<>();
					orderItem.setOrderToppingList(orderToppingList);
					orderItemList.add(orderItem);
				}
			}

			if (rs.getInt("i_id") != 0) {
				if (nowItemId != beforeItemId) {
					item = new Item();
					item.setId(rs.getInt("i_id"));
					item.setName(rs.getString("i_name"));
					item.setDescription(rs.getString("i_description"));
					item.setPriceM(rs.getInt("i_price_m"));
					item.setPriceL(rs.getInt("i_price_L"));
					item.setImagePath(rs.getString("i_image_path"));
					item.setDeleted(rs.getBoolean("i_deleted"));
					toppingList = new ArrayList<>();
					item.setToppingList(toppingList);
					orderItem.setItem(item);
				}
			}

			if (rs.getInt("ot_id") != 0) {
				if (nowOrderToppingId != beforeOrderToppingId) {
					orderTopping = new OrderTopping();
					orderTopping.setId(rs.getInt("ot_id"));
					orderTopping.setToppingId(rs.getInt("ot_topping_id"));
					orderTopping.setOrderItemId(rs.getInt("ot_order_item_id"));
					orderTopping.setTopping(topping);
					orderToppingList.add(orderTopping);
				}
			}

			if (rs.getInt("t_id") != 0) {
				if (nowToppingId != beforeToppingId) {
					topping = new Topping();
					topping.setId(rs.getInt("t_id"));
					topping.setName(rs.getString("t_name"));
					topping.setPriceM(rs.getInt("t_price_m"));
					topping.setPriceL(rs.getInt("t_price_l"));
					toppingList.add(topping);
					orderTopping.setTopping(topping);
				}
			}
			beforeOrderId = nowOrderId;
			beforeOrderItemId = nowOrderItemId;
			beforeOrderToppingId = nowOrderToppingId;
			beforeItemId = nowItemId;
			beforeToppingId = nowToppingId;
		}
		return order;
	};

	/**
	 * 主キー検索を行います.
	 * @param orderId 注文Id
	 * @return 注文情報
	 */
	public Order load(Integer orderId) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT id,user_id,status,total_price,order_date,destination_name,destination_email,destination_zipcode,");
		sql.append("destination_address,destination_tel,delivery_time,payment_method");
		sql.append(" FROM orders WHERE id=:orderId");
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
		Order order = template.queryForObject(sql.toString(), param, ORDER_ROW_MAPPER);
		return order;
	}

	/**
	 * 引数で受け取ったユーザーId、ステータス情報をもとに注文情報を検索します.
	 * 
	 * @param userId ユーザーId
	 * @param status ステータス
	 * @return 注文情報
	 */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT o.id o_id,o.user_id o_user_id,o.status o_status,o.total_price o_total_price,o.order_date o_order_date,");
		sql.append(
				"o.destination_name o_destination_name,o.destination_email o_destination_email,o.destination_zipcode o_destination_zipcode,");
		sql.append(
				"o.destination_address o_destination_address,o.destination_tel o_destination_tel,o.delivery_time o_delivery_time,o.payment_method o_payment_method,");
		sql.append(
				"oi.id oi_id,oi.item_id oi_item_id,oi.order_id oi_order_id,oi.quantity oi_quantity,oi.size oi_size,");
		sql.append("ot.id ot_id,ot.topping_id ot_topping_id,ot.order_item_id ot_order_item_id,");
		sql.append(
				"i.id i_id,i.name i_name,i.description i_description,i.price_m i_price_m,i.price_l i_price_l,i.image_path i_image_path,i.deleted i_deleted,");
		sql.append("t.id t_id,t.name t_name,t.price_m t_price_m,t.price_l t_price_l");
		sql.append(" FROM orders o LEFT OUTER JOIN order_items oi ON o.id = oi.order_id");
		sql.append(" LEFT OUTER JOIN order_toppings ot ON oi.id = ot.order_item_id");
		sql.append(" LEFT OUTER JOIN items i ON oi.item_id = i.id");
		sql.append(" LEFT OUTER JOIN toppings t ON ot.topping_id = t.id");
		sql.append(" WHERE o.user_id = :userId AND o.status = :status ORDER BY oi.id DESC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		Order order = template.query(sql.toString(), param, ORDER_RESULT_SET_EXTRACTOR);
		return order;
	}

	/**
	 * 注文情報を挿入する.
	 * 
	 * @param order 注文情報
	 * @return
	 */
	public Order insert(Order order) {
		String sql = "insert into orders (user_id,status,total_price) values(:userId,:status,:totalPrice);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
				.addValue("status", order.getStatus()).addValue("totalPrice", order.getTotalPrice());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		template.update(sql, param, keyHolder, keyColumnNames);
		order.setId(keyHolder.getKey().intValue());
		return order;
	}

	/**
	 * 渡した合計金額を更新する.
	 * 
	 * @param Order 更新する注文情報
	 */
	public void updateTotalPrice(Integer id, Integer totalPrice) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE orders SET total_price=:totalPrice WHERE id=:id");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("totalPrice", totalPrice);
		template.update(sql.toString(), param);
	}

	/**
	 * 注文情報を更新する.
	 * 
	 * @param order 更新する注文情報
	 */
	public void update(Order order) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"UPDATE orders SET status=:status,order_date=:orderDate,destination_name=:destinationName,destination_email=:destinationEmail,");
		sql.append(
				"destination_zipcode=:destinationZipcode,destination_address=:destinationAddress,destination_tel=:destinationTel,delivery_time=:deliveryTime,");
		sql.append("payment_method=:paymentMethod WHERE id = :id;");
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(sql.toString(), param);
	}

}
