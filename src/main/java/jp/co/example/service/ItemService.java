package jp.co.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.domain.Item;
import jp.co.example.repository.ItemRepository;

@Service
@Transactional
public class ItemService {
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 全商品を検索します.
	 * 
	 * @return 全商品が格納されたリスト
	 */
	public List<Item> searchAllItem() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

	/**
	 * 商品を主キー検索します.
	 * 
	 * @param itemId 主キー検索する商品Id
	 * @return 商品情報
	 */
	public Item showDetail(Integer itemId) {
		return itemRepository.load(itemId);
	}

	/**
	 * 引数で受け取った名前をもとに曖昧検索を行います.
	 * 
	 * @param name 名前
	 * @return 商品情報
	 */
	public List<Item> searchByLikeName(String name) {
		List<Item> itemList = itemRepository.findByLikeName(name);
		return itemList;
	}

	/**
	 * 金額の昇順または降順で商品一覧を取得します.
	 * 
	 * @return 商品リスト
	 */
	public List<Item> searchAllItemOrderByPrice(String searchOrderNum) {
		List<Item> itemList = null;
		if ("1".equals(searchOrderNum)) {
			itemList = itemRepository.findAllOrderByPriceASC();
		} else if ("2".equals(searchOrderNum)) {
			itemList = itemRepository.findAllOrderByPriceDESC();
		}
		return itemList;
	}

	/**
	 * 金額の昇順または降順で商品一覧を取得します.
	 * 
	 * @param name           検索商品名
	 * @param searchOrderNum １の場合金額昇順、２の場合金額降順
	 * @return 商品一覧
	 */
	public List<Item> searchByLikeNameOrderByPrice(String name, String searchOrderNum) {
		List<Item> itemList = null;
		if ("1".equals(searchOrderNum)) {
			itemList = itemRepository.findByLikeNameOrderByPriceASC(name);
		} else if ("2".equals(searchOrderNum)) {
			itemList = itemRepository.findByLikeNameOrderByPriceDESC(name);
		}
		return itemList;
	}

}
