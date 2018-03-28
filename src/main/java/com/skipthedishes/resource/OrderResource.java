package com.skipthedishes.resource;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skipthedishes.dto.OrderDto;
import com.skipthedishes.events.CreatedResourceEvent;
import com.skipthedishes.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderResource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OrderService orderService;
	
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public OrderResource( final OrderService orderService, final ApplicationEventPublisher publisher) {
		this.orderService = orderService;
		this.publisher = publisher;
	}
	
	@PostMapping
	public ResponseEntity<OrderDto> create(@RequestBody final OrderDto order, final HttpServletResponse response, final HttpServletRequest request, final Authentication authentication) {
		final OrderDto orderSaved = this.orderService.save(order);
		publisher.publishEvent(new CreatedResourceEvent(this, response, orderSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(orderSaved);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> findById(@PathVariable("id") final Integer id) {
		final OrderDto storeFound = this.orderService.findById(id);	
		return new ResponseEntity<OrderDto>(storeFound, HttpStatus.OK);
	}
	
	@GetMapping("/status")
	public List<OrderDto> listAllByStatus(final Pageable pageable) {
		return this.orderService.findAllOrderStatus(pageable);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<OrderDto> cancelOrder(@PathVariable("id") @NotNull final Integer id) {
		final OrderDto orderCanceled = this.orderService.cancelOrder(id); 
		return ResponseEntity.ok().body(orderCanceled);   
	}

}
