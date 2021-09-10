package jp.co.example.domain;

public enum PaymentMethod {
	CASH_ON_DELIVERY(1,"代金引換")
	,CREDIT_CARD(2, "クレジットカード");
	
	private final Integer key;
	private final String value;
	
	private PaymentMethod(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	public static PaymentMethod of(Integer key) {
		for(PaymentMethod paymentMethod: PaymentMethod.values()) {
			if(paymentMethod.key == key) {
				return paymentMethod;
			}
		}
		throw new IndexOutOfBoundsException("The value of status doesn't exist;");
	}
}
