package jp.co.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.domain.OrderTopping;

@Repository
public class OrderToppingRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * トッピングを挿入します.
	 * 
	 * @param orderTopping 注文したトッピング情報
	 */
	public void insert(OrderTopping orderTopping) {
		String sql = "INSERT INTO order_toppings (topping_id,order_item_id) VALUES (:toppingId,:orderItemId);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		template.update(sql, param);
	}
}
