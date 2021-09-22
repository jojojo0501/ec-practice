package jp.co.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		sql.append(" FROM items WHERE deleted = false ORDER BY id DESC;");
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
		sql.append(" FROM items WHERE id=:id AND deleted = false;");
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
		sql.append(" FROM items WHERE name ILIKE :name AND deleted = false ORDER BY id;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}

	/**
	 * 商品リストを値段の昇順で取得します.
	 * 
	 * @return 商品リスト
	 */
	public List<Item> findAllOrderByPriceASC() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE deleted = false ORDER BY price_m ASC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 商品リストを値段の降順で取得します.
	 * 
	 * @return 商品リスト
	 */
	public List<Item> findAllOrderByPriceDESC() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE deleted = false ORDER BY price_m DESC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 商品リストを値段の昇順で取得します.
	 * 
	 * @param name 検索名
	 * @return 商品リスト
	 */
	public List<Item> findByLikeNameOrderByPriceASC(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE name ILIKE :name AND deleted = false ORDER BY price_m ASC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}

	/**
	 * 商品リストを値段の降順で取得します.
	 * 
	 * @param name 検索名
	 * @return 商品名
	 */
	public List<Item> findByLikeNameOrderByPriceDESC(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE name ILIKE :name AND deleted = false ORDER BY price_m DESC;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}

	/**
	 * 新商品を登録します.
	 * 
	 * @param item 商品情報
	 * @return 新商品情報
	 */
	synchronized public Item insert(Item item) {
		item.setId(getPrimaryId());
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO items(id,name,description,price_m,price_l,image_path)");
		insertSql.append(" VALUES(:id,:name,:description,:priceM,:priceL,:imagePath);");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = { "id" };
		template.update(insertSql.toString(), param, keyHolder, keyColumnNames);
		item.setId(keyHolder.getKey().intValue());
		return item;
	}

	/**
	 * 商品テーブルの中で一番大きいID＋1（プライマリーキー）を取得する.
	 * 
	 * @return テーブル内で一番値が大きいID+1.データがない場合はi
	 */
	private Integer getPrimaryId() {
		try {
			Integer maxId = template.queryForObject("SELECT MAX(id) FROM items;", new MapSqlParameterSource(),
					Integer.class);
			return maxId + 1;
		} catch (DataAccessException e) {
			// データが存在しない場合
			return 1;
		}
	}

	/**
	 * 商品をのdeleteフラグを更新する(論理削除).
	 * 
	 * @param Item 更新する商品情報
	 */
	public void updateDeleteFlg(Item item) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE items SET deleted=:deleted WHERE id=:id");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", item.getId()).addValue("deleted",
				item.getDeleted());
		template.update(sql.toString(), param);
	}

	/**
	 * 全件検索します.
	 * 
	 * @return 全件の情報が格納されたリスト.
	 */
	public List<Item> adminFindAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items ORDER BY id DESC;");
		List<Item> itemList = template.query(sql.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 主キー検索をします.
	 * 
	 * @param itemId itemのid
	 * @return アイテム
	 */
	public Item adminLoad(Integer itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted");
		sql.append(" FROM items WHERE id=:id;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", itemId);
		Item item = template.queryForObject(sql.toString(), param, ITEM_ROW_MAPPER);
		return item;
	}


}
