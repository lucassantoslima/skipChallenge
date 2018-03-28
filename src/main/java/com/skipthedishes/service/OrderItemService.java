package com.skipthedishes.service;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.skipthedishes.dto.OrderItemDto;
import com.skipthedishes.model.Order;
import com.skipthedishes.model.OrderItem;
import com.skipthedishes.model.Product;

@Service
@Lazy
public class OrderItemService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProductService productService;
	
	@Autowired
	public OrderItemService(final ProductService productService) {
		this.productService = productService;
	}
	
	public OrderItem save( final OrderItemDto itemDto, final Order order ) {
		final OrderItem itemToBeSave = new OrderItem();
		BeanUtils.copyProperties(itemDto, itemToBeSave, "id");
		
		Product productfound = this.productService.validateNotFoundProduct(itemDto.getProductId()); 
		itemToBeSave.setProduct(productfound);  
		itemToBeSave.setPrice(productfound.getPrice()); 
		
		this.calculateTotalOfItem(productfound, itemToBeSave); 
		
		order.addItem( itemToBeSave );
		
		return itemToBeSave;
	}
	
	private void calculateTotalOfItem(final Product product, OrderItem item) {
		item.setTotal(product.getPrice() * item.getQuantity()); 
	}

}
