package com.skipthedishes.enuns;

public enum OrderType {
	
	DELIVERY, PICKUP;

	public static OrderType fromString(final String s) {
		return OrderType.valueOf(s);
	}

}
