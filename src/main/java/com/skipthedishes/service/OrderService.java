package com.skipthedishes.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skipthedishes.dto.OrderDto;
import com.skipthedishes.enuns.OrderStatus;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;
import com.skipthedishes.model.Customer;
import com.skipthedishes.model.Order;
import com.skipthedishes.model.OrderItem;
import com.skipthedishes.model.Store;
import com.skipthedishes.repository.OrderRepository;
import com.skipthedishes.security.IAuthenticationFacade;
import com.skipthedishes.service.factory.OrderTypeFactory;

@Service
@Lazy
public class OrderService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OrderRepository orderRepository;
	private ModelMapper modelMapper;
	private StoreService storeService;
	private CustomerService customerService;
	private OrderItemService orderItemService;
	private MessageSource messageSource;
	private IAuthenticationFacade authenticationFacade;
	
	public OrderService() {
	}
	
	@Autowired
	public OrderService(final OrderRepository orderRepository, final ModelMapper modelMapper, 
			final StoreService storeService, 
			final CustomerService customerService,
			final OrderItemService orderItemService,
			final MessageSource messageSource,
			final IAuthenticationFacade authenticationFacade) {
		
		this.orderRepository = orderRepository;
		this.modelMapper = modelMapper;
		this.storeService = storeService;
		this.customerService = customerService;
		this.orderItemService = orderItemService;
		this.messageSource = messageSource;
		this.authenticationFacade = authenticationFacade;
	}
	
	private void validateDuplicateOrder( final Integer id) {
		if( StringUtils.isEmpty(id)) {
			return;
		}
		
		this.orderRepository.findById(id).ifPresent( c -> { throw new DuplicateResourceException( messageSource.getMessage("order.already.exists", new Object[]{id}, LocaleContextHolder.getLocale()) ); });
	}
	
	private void validateNotFoundOrder(final Integer id) {
		this.orderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}
	
	public OrderDto findById(final Integer id) {
		this.validateNotFoundOrder(id); 
		Optional<Order> cousineFound = orderRepository.findById(id);
		return modelMapper.map(cousineFound.get(), OrderDto.class);
	}
	
	public List<OrderDto> findAllOrderStatus( final Pageable pageable ){
		final Page<Order> orders = this.orderRepository.findAllOrderStatus(pageable);
		Page<OrderDto> map = orders.map(c -> this.modelMapper.map(c, OrderDto.class));
		return map.getContent(); 
	} 
	
	public OrderDto cancelOrder( final Integer id ) {
		this.validateNotFoundOrder(id); 
		
		Order orderFound = this.orderRepository.findById(id).get();
		
		orderFound.setOrderStatus(OrderStatus.CANCELED);
		
		Order orderCanceled = this.orderRepository.save(orderFound);
		 
		return new OrderDto(orderCanceled.getId(), orderCanceled.getCustomer().getId(), orderCanceled.getStore().getId(), orderCanceled.getTotal(), orderCanceled.getOrderStatus()); 
	}
	
	public OrderDto save( final OrderDto orderDto ) { 
		this.validateDuplicateOrder( orderDto.getId() );
		
		final Order order = new Order();
		BeanUtils.copyProperties(orderDto, order, "orderItems");  

		Store storeFound = this.storeService.validateNotFoundStore(orderDto.getStoreId()); 
		order.setStore(storeFound);    
		
		Customer customer = this.customerService.validateNotFoundCustomer(authenticationFacade.getAuthentication().getName());
		order.setCustomer(customer); 
		
		order.setDate(LocalDateTime.now());  
		
		orderDto.getOrderItems().forEach(itemDto -> {
			OrderItem itemToBeSave = this.orderItemService.save(itemDto, order); 
			this.calculateItems(order, itemToBeSave); 
		});
		
		this.calculateTotalOfOrder(order);  
		
		Order orderSaved = this.orderRepository.save(order);
		
		return this.modelMapper.map(orderSaved, OrderDto.class); 
	}

	private void calculateTotalOfOrder(final Order order) { 
		OrderTypeFactory factory = OrderTypeFactory.getFactory(order.getOrderType()); 
		final BigDecimal calculatedOrder = factory.calculateOrderType(order.getTotal());		
		order.setTotal( order.getTotal().add(calculatedOrder).setScale(2, RoundingMode.HALF_UP) ); 
	}

	private void calculateItems(final Order order, final OrderItem itemToBeSave) {
		order.setTotal( order.getTotal().add(new BigDecimal(itemToBeSave.getTotal())).setScale(2, RoundingMode.HALF_UP) );
	}

}
