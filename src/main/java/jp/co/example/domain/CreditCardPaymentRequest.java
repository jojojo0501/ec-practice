package jp.co.example.domain;

public class CreditCardPaymentRequest {
	/** ユーザID */
	private long user_id;
	/** 注文番号 */
	private String oeder_number;
	/** 決済金額 */
	private String amount;
	/** カード番号 */
	private String card_number;
	/** カード有効期限(年) */
	private String card_exp_year;
	/** カード有効期限(月) */
	private String card_exp_month;
	/** セキュリティコード */
	private String card_cvv;
	/** カード名義人名 */
	private String card_name;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getOeder_number() {
		return oeder_number;
	}

	public void setOeder_number(String oeder_number) {
		this.oeder_number = oeder_number;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(String card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public String getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(String card_exp_month) {
		this.card_exp_month = card_exp_month;
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

	@Override
	public String toString() {
		return "CreditCardPaymentDomain [user_id=" + user_id + ", oeder_number=" + oeder_number + ", amount=" + amount
				+ ", card_number=" + card_number + ", card_exp_year=" + card_exp_year + ", card_exp_month="
				+ card_exp_month + ", card_cvv=" + card_cvv + ", card_name=" + card_name + "]";
	}
}
