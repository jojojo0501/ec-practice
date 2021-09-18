package jp.co.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.domain.Item;

@Repository
public class ItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Itemオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);

	/**
	 * 全件検索します.
	 * 
	 * @return 全件の情報が格納されたリスト.
	 */
	public List<Item> findAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items ORDER BY id;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 主キー検索をします.
	 * 
	 * @param itemId itemのid
	 * @return アイテム
	 */
	public Item load(Integer itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE id=:id;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", itemId);
		Item item = template.queryForObject(sql.toString(), param, ITEM_ROW_MAPPER);
		return item;
	}

	/**
	 * 引数で受け取った名前をもとに曖昧検索します.
	 * 
	 * @param name 名前
	 * @return 商品情報
	 */
	public List<Item> findByLikeName(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE name ILIKE :name ORDER BY id;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}
	
	/**
	 * 商品リストを値段の昇順で取得します.
	 * @return 商品リスト
	 */
	public List<Item> findAllOrderByPriceASC() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items ORDER BY price_m ASC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}
	
	public List<Item> findAllOrderByPriceDESC() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items ORDER BY price_m DESC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

}
