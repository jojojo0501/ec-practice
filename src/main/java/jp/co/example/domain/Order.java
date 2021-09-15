package jp.co.example.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Order {
	private Integer id;
	private Integer userId;
	private Integer status;
	private Integer totalPrice;
	private Date orderDate;
	private String destinationName;
	private String destinationEmail;
	private String destinationZipcode;
	private String destinationAddress;
	private String destinationTel;
	private Timestamp deliveryTime;
	private Integer paymentMethod;
	private User user;
	private List<OrderItem> orderItemList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public String getDestinationZipcode() {
		return destinationZipcode;
	}

	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getDestinationTel() {
		return destinationTel;
	}

	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}

	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", status=" + status + ", totalPrice=" + totalPrice
				+ ", orderDate=" + orderDate + ", destinationName=" + destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationZipcode=" + destinationZipcode + ", destinationAddress="
				+ destinationAddress + ", destinationTel=" + destinationTel + ", deliveryTime=" + deliveryTime
				+ ", paymentMethod=" + paymentMethod + ", user=" + user + ", orderItemList=" + orderItemList + "]";
	}

	/**
	 * ショッピングカート内の商品の合計金額（税抜）を算定します.
	 * 
	 * @return 合計金額（税抜）
	 */
	public Integer getCalcSubTotalPrice() {
		Integer subTotalPrice = 0;
		try {
			for (OrderItem orderItem : this.orderItemList) {
				subTotalPrice += orderItem.getSubTotal();
			}
		} catch (NullPointerException e) {
			subTotalPrice = 0;
		}
		return subTotalPrice;
	}

	/**
	 * 消費税額を計算します.
	 * 
	 * @return 消費税額
	 */
	public Integer getTax() {
		return Math.round((this.getCalcSubTotalPrice() * 0.1F));
	}

	/**
	 * ショッピングカート内の商品の合計金額（税込）を算定します.
	 * 
	 * @return 合計金額（税込）
	 */
	public Integer getCalcTotalPrice() {
		int tax = 10 * (int) Math.floor(this.getTax() * 0.1);
		return this.getCalcSubTotalPrice() + tax;
	}

}
