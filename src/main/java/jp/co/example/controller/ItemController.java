package jp.co.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.Item;
import jp.co.example.service.ItemService;
import jp.co.example.service.ToppingService;

@Controller
@RequestMapping("/")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ToppingService toppingService;

	/**
	 * 商品一覧画面を表示します.
	 * 
	 * @return 商品一覧画面へフォワードします.
	 */
	@RequestMapping("")
	public String index(Model model) {
		int count = 0;
		List<Item> itemList = itemService.searchAllItem();
		List<List<Item>> wrapperItemList = new ArrayList<>();
		List<Item> threeItemList = new ArrayList<>();
		for (Item item : itemList) {
			threeItemList.add(item);
			count++;
			if (count % 3 == 0) {
				wrapperItemList.add(threeItemList);
				threeItemList = new ArrayList<>();
			}
		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("wrapperItemList", wrapperItemList);
		return "item_list_pizza";
	}

	/**
	 * 商品詳細画面を表示します.
	 * 
	 * @param id    商品ID
	 * @param model リクエストスコープに商品情報を格納する。
	 * @return 商品詳細ページへフォワードする。
	 */
	@RequestMapping("/detail")
	public String showDetail(Integer id, Model model) {
		Item item = itemService.showDetail(id);
		item.setToppingList(toppingService.searchAll());
		model.addAttribute("item", item);
		return "item_detail";
	}

	/**
	 * 引数で受け取った商品名をもとに曖昧検索を行います.
	 *  searchOrderNumが0ならid順、1なら金額昇順、２なら金額降順
	 * @param name  商品名
	 * @param model リクエストスコープに商品情報を格納します。
	 * @return 商品一覧ページへフォワードします。
	 */
	@RequestMapping("/search")
	public String searchByItemName(String name, String searchOrderNum, Model model) {
		List<Item> itemList = null;
		if (!("0".equals(searchOrderNum)) && !("".equals(name))) {
			itemList = itemService.searchByLikeNameOrderByPrice(name, searchOrderNum);
		} else if (!("0".equals(searchOrderNum)) && ("".equals(name))) {
			itemList = itemService.searchAllItemOrderByPrice(searchOrderNum);
		} else if ("0".equals(searchOrderNum) && ("".equals(name))) {
			model.addAttribute("ItemMessage", "該当する商品がありません。");
			itemList = itemService.searchAllItem();
		} else {
			itemList = itemService.searchByLikeName(name);
		}
		if("1".equals(searchOrderNum)) {
			model.addAttribute("ItemMessage", "金額昇順で検索しました！");
		}else if("2".equals(searchOrderNum)) {
			model.addAttribute("ItemMessage", "金額降順で検索しました！");						
		}
		if (itemList == null) {
			model.addAttribute("ItemMessage", "該当する商品がありません。");
			itemList = itemService.searchAllItem();
		}
		int count = 0;
		List<List<Item>> wrapperItemList = new ArrayList<>();
		List<Item> threeItemList = new ArrayList<>();
		for (Item item : itemList) {
			threeItemList.add(item);
			count++;
			if (count % 3 == 0) {
				wrapperItemList.add(threeItemList);
				threeItemList = new ArrayList<>();
			}
		}
		if (count % 3 != 0) {
			wrapperItemList.add(threeItemList);
		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("wrapperItemList", wrapperItemList);
		return "item_list_pizza";
	}

}
