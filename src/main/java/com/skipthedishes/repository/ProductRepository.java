package com.skipthedishes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skipthedishes.model.Product;

@Repository 
public interface ProductRepository extends JpaRepository<Product, Integer> {

	 @Query("select p from Product p where p.name like CONCAT('%',:pname,'%')")
	 public abstract Page<Product> findAllProductsByName(@Param("pname") String searchText, Pageable pageable);
	    
	
}
