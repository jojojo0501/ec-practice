package jp.co.example.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.example.domain.Item;
import jp.co.example.form.RegisterItemForm;
import jp.co.example.service.ItemService;
import jp.co.example.service.ToppingService;

@Controller
@RequestMapping("/")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ToppingService toppingService;

	@ModelAttribute
	public RegisterItemForm setUpRegisterItemForm() {
		return new RegisterItemForm();
	}

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
		if (count % 3 != 0) {
			wrapperItemList.add(threeItemList);
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
	 * 引数で受け取った商品名をもとに曖昧検索を行います. searchOrderNumが0ならid順、1なら金額昇順、２なら金額降順
	 * 
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
		if ("1".equals(searchOrderNum)) {
			model.addAttribute("ItemMessage", "金額昇順で検索しました！");
		} else if ("2".equals(searchOrderNum)) {
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

	/**
	 * 商品追加フォームページヘ遷移します.
	 * 
	 * @return 商品追加ページへフォワードします。
	 */
	@RequestMapping("/item/add-form")
	public String toAddItemForm() {
		return "item_add_form";
	}

	/**
	 * 商品を追加します.
	 * 
	 * @param form 入力された商品情報
	 * @return 商品一覧ページへリダイレクトする。
	 */
	@RequestMapping("/item/register")
	public String registerItem(@Validated RegisterItemForm form, BindingResult result) throws IOException {
		// 画像ファイル形式チェック
		MultipartFile imageFile = form.getImageFile();
		String fileExtension = null;
		try {
			fileExtension = getExtension(imageFile.getOriginalFilename());

			if (!"jpg".equals(fileExtension) && !"png".equals(fileExtension)) {
				result.rejectValue("imageFile", "", "拡張子は.jpgか.pngのみに対応しています");
			}
		} catch (Exception e) {
			result.rejectValue("imageFile", "", "拡張子は.jpgか.pngのみに対応しています");
		}
		itemService.registerItem(form, fileExtension);
		return "redirect:/";
	}

	/*
	 * ファイル名から拡張子を返します.
	 * 
	 * @param originalFileName ファイル名
	 * 
	 * @return .を除いたファイルの拡張子
	 */
	private String getExtension(String originalFileName) throws Exception {
		if (originalFileName == null) {
			throw new FileNotFoundException();
		}
		int point = originalFileName.lastIndexOf(".");
		if (point == -1) {
			throw new FileNotFoundException();
		}
		return originalFileName.substring(point + 1);
	}

}
