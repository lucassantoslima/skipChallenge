package com.skipthedishes.service.factory;

import java.math.BigDecimal;

public interface IOrderType {
	BigDecimal calculateOrderType(final BigDecimal total);
}
