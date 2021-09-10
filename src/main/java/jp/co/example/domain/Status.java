package jp.co.example.domain;

public enum Status {
	BEFORE_ORDER(0,"注文前")
	,NOT_PAYMENT(1, "入金前")
	,DEPOSITED(2, "入金済")
	,DELIVERED(3, "配送済")
	,DELIVERY_COMPLETE(4, "配送完了")
	,CANCEL_ORDER(9,"キャンセル");
	
	private final Integer key;
	private final String value;
	
	private Status(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}

	public static Status of(Integer key) {
		for(Status status: Status.values()) {
			if(status.key.equals(key)) {
				return status;
			}
		}
		throw new IndexOutOfBoundsException("The value of status doesn't exsist");
	}
}
