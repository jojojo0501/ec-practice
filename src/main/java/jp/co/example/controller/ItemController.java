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
	 * 
	 * @param name  商品名
	 * @param model リクエストスコープに商品情報を格納します。
	 * @return 商品一覧ページへフォワードします。
	 */
	@RequestMapping("/search")
	public String searchByItemName(String name, Model model) {
		List<Item> itemList = itemService.searchByLikeName(name);
		if (itemList == null || "".equals(name)) {
			model.addAttribute("noItemMessage", "該当する商品がありません。");
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
		if(count % 3 != 0) {
			wrapperItemList.add(threeItemList);
		}
		if (count < 3) {
			wrapperItemList.add(threeItemList);
		}
		model.addAttribute("wrapperItemList", wrapperItemList);
		return "item_list_pizza";
	}

}
