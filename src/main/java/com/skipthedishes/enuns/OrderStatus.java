package com.skipthedishes.enuns;

public enum OrderStatus {
	WAITING,DELIVERED,CANCELED;

	public static OrderStatus fromString(final String s) {
		return OrderStatus.valueOf(s);
	}
}
