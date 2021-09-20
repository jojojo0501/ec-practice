package jp.co.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
	
	/**
	 * 商品リストを値段の降順で取得します.
	 * @return 商品リスト
	 */
	public List<Item> findAllOrderByPriceDESC() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items ORDER BY price_m DESC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 商品リストを値段の昇順で取得します.
	 * @param name 検索名
	 * @return 商品リスト
	 */
	public List<Item> findByLikeNameOrderByPriceASC(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE name ILIKE :name ORDER BY price_m ASC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}
	
	/**
	 * 商品リストを値段の降順で取得します.
	 * @param name 検索名
	 * @return　商品名
	 */
	public List<Item> findByLikeNameOrderByPriceDESC(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE name ILIKE :name ORDER BY price_m DESC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}
	
	/**
	 * 新商品を登録します.
	 * @param item 商品情報
	 * @return　新商品情報
	 */
	public Item insert(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO items(name,description,price_m,price_l,image_path)");
		insertSql.append("VALUES(:name,:description,:priceM,:priceL,:imagePath);");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		template.update(insertSql.toString(), param, keyHolder, keyColumnNames);
		item.setId(keyHolder.getKey().intValue());
		return item;
	}
	
	

}
