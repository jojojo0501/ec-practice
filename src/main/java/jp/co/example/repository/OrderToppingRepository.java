package jp.co.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
	public OrderTopping insert(OrderTopping orderTopping) {
		String sql = "INSERT INTO order_toppings (topping_id,order_item_id) VALUES (:toppingId,:orderItemId);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnName = { "id" };
		template.update(sql, param, keyHolder, keyColumnName);
		orderTopping.setId(keyHolder.getKey().intValue());
		return orderTopping;
	}
}
