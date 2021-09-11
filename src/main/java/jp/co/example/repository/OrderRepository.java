package jp.co.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.domain.Order;

@Repository
public class OrderRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final ResultSetExtractor<Order> ORDER_RESULT_SET_EXTRACTOR = (rs) -> {
		return null;
	};
	
	/**
	 * 引数で受け取ったユーザーId、ステータス情報をもとに注文情報を検索します.
	 * @param userId ユーザーId
	 * @param status　ステータス
	 * @return 注文情報
	 */
	public Order findByUserIdAndStatus(Integer userId,Integer status) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id o_id,o.user_id o_user_id,o.status o_status,o.total_price o_total_price,o.order_date o_order_date,");
		sql.append("o.destination_name o_destination_name,o.destination_email o_destination_email,o.destination_zipcode o_destination_zipcode,");
		sql.append("o.destination_address o_destination_address,o.destination_tel o_destination_tel,o.delivery_time o_delivery_time,o.payment_method o_payment_method,");
		sql.append("oi.id oi_id,oi.item_id oi_item_id,oi.order_id oi_order_id,oi.quantity oi_quantity,oi.size oi_size,");
		sql.append("ot.id ot_id,ot.topping_id ot_topping_id,ot.order_item_id ot_order_item_id,");
		sql.append("i.id i_id,i.name i_name,i.price_m i_price_m,i.price_l i_price_l,");
		sql.append("t.id t_id,t.name t_name,t.price_m t_price_m,t.price_l t_price_l");
		sql.append("FROM orders o LEFT OUTER JOIN order_items oi ON o.id = oi.order_id");
		sql.append("LEFT OUTER JOIN order_toppings ot ON oi.id = ot.order_item_id");
		sql.append("LEFT OUTER JOIN items i ON oi.item_id = i.id");
		sql.append("LEFT OUTER JOIN toppings t ON ot.topping_id = t.id;");
		sql.append("WHERE o.user_id = :userId AND o.status = :status ORDER BY oi.id DESC");
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		Order order = template.query(sql.toString(), param,ORDER_RESULT_SET_EXTRACTOR);
		return order;
	}
	
	
}
