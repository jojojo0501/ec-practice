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
	 * @param itemId 主キー検索する商品Id
	 * @return 商品情報
	 */
	public Item showDetail(Integer itemId) {
			return itemRepository.load(itemId);
		}

}
