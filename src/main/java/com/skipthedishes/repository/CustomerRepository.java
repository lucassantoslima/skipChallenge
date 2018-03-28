package com.skipthedishes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skipthedishes.model.Customer;

@Repository 
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	public abstract Optional<Customer> findByEmail( final String email );
	
}
