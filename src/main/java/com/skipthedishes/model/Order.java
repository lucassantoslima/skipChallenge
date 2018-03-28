package com.skipthedishes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.skipthedishes.enuns.OrderStatus;
import com.skipthedishes.enuns.OrderType;

@Entity
@Table(name = "TB_ORDER")
public class Order implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, precision = 8, name = "ID")
	private Integer id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@NotBlank
	@Column(name = "delivert_address")
	private String deliveryAddress;
	
	@NotBlank
	private String contact;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	
	@OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
	private List<OrderItem> orderItems;

	private BigDecimal total;
	
	@Column(name = "order_date")
	private LocalDateTime date;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "order_type")
	private OrderType orderType;
	
	public Order() {
	}

	public Order(Integer id, @NotNull OrderStatus orderStatus, @NotNull OrderType orderType, BigDecimal total) {
		super();
		this.id = id;
		this.orderStatus = orderStatus;
		this.orderType = orderType;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<OrderItem> getOrderItems() {
		if( orderItems == null ) {
			orderItems = new ArrayList<OrderItem>();
		}
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public BigDecimal getTotal() {
		if( total == null ) {
			this.total = BigDecimal.ZERO;
		}
		
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public boolean addItem( final OrderItem item ) {
		item.setOrder(this); 
		return getOrderItems().add(item); 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Order))
			return false;
		Order other = (Order) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
