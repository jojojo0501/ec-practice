package jp.co.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class OrderForm {
	private String orderId;
	@NotBlank(message="名前を入力してください。")
	private String destinationName;
	@NotBlank(message="メールアドレスを入力してください。")
	@Email(message="メールアドレスの形式が不正です。")
	private String destinationEmail;
	@NotBlank(message="郵便番号を入力してください。")
	@Pattern(regexp="^[0-9]{7}$",message="郵便番号は７桁（ハイフン無し）で入力してください。")
	private String destinationZipcode;
	@NotBlank(message="住所を入力してください。")
	private String destinationAddress;
	@NotBlank(message="電話番号を入力してください。")
	@Pattern(regexp="^0\\d{9,10}$",message="電話番号の形式で入力してください。")
	private String destinationTel;
	@NotBlank(message="配達日時を入力してください。")
	private String deliveryDate;
	private String deliveryTime;
	private String paymentMethod;
	
	//以下クレジットカード入力の場合に送られる。
	/** 購入者ID */
	private String user_id;
	/** 注文番号 */
	private String order_number;
	/** 決済金額(数字10桁) */
	private String amount;
	/** カード番号*/
	private String card_number;
	/** カード有効期限*/
	private String card_exp_month;
	/** カード有効期限(年)*/
	private String card_exp_year;
	/** セキュリティコード */
	private String card_cvv;
	/** カード名義人名 */
	private String card_name;
	
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

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}
	public Integer getIntPaymentMethod() {
		return Integer.parseInt(paymentMethod);
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOrderId() {
		return orderId;
	}

	public Integer getIntOrderId() {
		return Integer.parseInt(orderId);
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(String card_exp_month) {
		this.card_exp_month = card_exp_month;
	}

	public String getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(String card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public String getCard_cvv() {
		return card_cvv;
	}

	public void setCard_cvv(String card_cvv) {
		this.card_cvv = card_cvv;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "OrderForm [orderId=" + orderId + ", destinationName=" + destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationZipcode=" + destinationZipcode + ", destinationAddress="
				+ destinationAddress + ", destinationTel=" + destinationTel + ", deliveryDate=" + deliveryDate
				+ ", deliveryTime=" + deliveryTime + ", paymentMethod=" + paymentMethod + ", user_id=" + user_id
				+ ", order_number=" + order_number + ", amount=" + amount + ", card_number=" + card_number
				+ ", card_exp_month=" + card_exp_month + ", card_exp_year=" + card_exp_year + ", card_cvv=" + card_cvv
				+ ", card_name=" + card_name + "]";
	}
}
