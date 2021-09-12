package jp.co.example.domain;

import java.util.List;

public class OrderItem {
	private Integer id;
	private Integer itemId;
	private Integer orderId;
	private Integer quantity;
	private Character size;
	private Item item;
	private List<OrderTopping> orderToppingList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public void setSizeStringFromChar(String size) {
		this.size = size.charAt(0);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 商品サイズとトッピングの数をもとに商品合計金額（税抜）を算定します.
	 * 
	 * @return 商品合計金額（税抜）
	 */
	public Integer getSubTotal() {
		Integer subTotalPrice = 0;
		Integer quantity = this.quantity;
		Integer ToppingCount = this.orderToppingList.size();
		if (this.size == 'M') {
			subTotalPrice += this.item.getPriceM() * quantity;
			subTotalPrice += ToppingCount * 200;
		} else if (this.size == 'L') {
			subTotalPrice += this.item.getPriceL() * quantity;
			subTotalPrice += ToppingCount * 300;
		}
		return subTotalPrice;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}
}
