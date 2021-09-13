package jp.co.example.form;

import java.util.List;

public class AddCartForm {
	private String itemId;
	private String size;
	private String quantity;
	private List<String> toppingIdList;

	public String getItemId() {
		return itemId;
	}

	public Integer getIntItemId() {
		return Integer.parseInt(itemId);
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSize() {
		return size;
	}

	public Character getCharSize() {
		return size.charAt(0);
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getQuantity() {
		return quantity;
	}

	public Integer getIntQuantity() {
		return Integer.parseInt(quantity);
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public List<String> getToppingIdList() {
		return toppingIdList;
	}

	public void setToppingIdList(List<String> toppingIdList) {
		this.toppingIdList = toppingIdList;
	}

	@Override
	public String toString() {
		return "AddCartForm [itemId=" + itemId + ", size=" + size + ", quantity=" + quantity + ", toppingIdList="
				+ toppingIdList + "]";
	}

}
