package com.skipthedishes.service.factory;

import com.skipthedishes.enuns.OrderType;

public abstract class OrderTypeFactory implements IOrderType {
	
	 public static OrderTypeFactory getFactory(OrderType orderType) {
		 OrderTypeFactory factory = null;	        
	        switch (orderType) {
	            case DELIVERY:
	                factory = new CalculateOrderTypeDelivery();
	                break;
	            case PICKUP:
	                factory = new CalculateOrderTypePickup(); 
	                break;
	        }
	        return factory;
	    }
}
