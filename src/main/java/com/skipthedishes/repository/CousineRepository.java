package com.skipthedishes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skipthedishes.model.Cousine;
import com.skipthedishes.model.Store;

@Repository 
public interface CousineRepository extends JpaRepository<Cousine, Integer> {
	
	public abstract Optional<Cousine> findByName( final String name );
    
    @Query("select s from Store s where s.cousine.id = :cousineId")
    public abstract Page<Store> findStores(@Param("cousineId") Integer cousineId, Pageable pageable);
    

}
