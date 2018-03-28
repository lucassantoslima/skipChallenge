package com.skipthedishes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skipthedishes.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> { 
	
	@Query("select new com.skipthedishes.model.Order(o.id, o.orderStatus, o.orderType, o.total) from Order o")
	public abstract Page<Order> findAllOrderStatus( Pageable pageable );
	
}
