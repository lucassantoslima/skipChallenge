package com.skipthedishes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skipthedishes.model.Store;

@Repository  
public interface StoreRepository extends JpaRepository<Store, Integer>  {

	public abstract Optional<Store> findByName( final String name );
    
    @Query("select p from Product p where p.store.id = :storeId") 
    public abstract Page<Store> findAllProducts(@Param("storeId") Integer storeId, Pageable pageable);
    
}
