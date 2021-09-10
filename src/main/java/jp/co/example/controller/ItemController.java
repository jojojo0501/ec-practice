package jp.co.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.domain.Item;
import jp.co.example.service.ItemService;

@Controller
@RequestMapping("/")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * 商品一覧画面を表示します.
	 * @return 商品一覧画面へフォワードします.
	 */
	@RequestMapping("")
	public String index(Model model) {
		int count = 0;
		List<Item> itemList = itemService.searchAllItem();
		List<List<Item>> wrapperItemList = new ArrayList<>();
		List<Item> threeItemList = new ArrayList<>();
		for(Item item:itemList) {
			threeItemList.add(item);
			count++;
			if(count % 3 == 0) {
				wrapperItemList.add(threeItemList);
				threeItemList = new ArrayList<>();
			}
		}
		model.addAttribute("wrapperItemList", wrapperItemList);
		return "item_list_pizza";
	}
	
	/**
	 * 商品詳細画面を表示します.
	 * @param id 商品ID
	 * @param model リクエストスコープに商品情報を格納する。
	 * @return　商品詳細ページへフォワードする。
	 */
	@RequestMapping("/detail")
	public String showDetail(Integer id,Model model) {
		Item item = itemService.showDetail(id);
		model.addAttribute("item", item);
		return "item_detail";
	}
}
