package com.skipthedishes.ut;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.skipthedishes.model.Cousine;
import com.skipthedishes.repository.CousineRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CousineUT {
	
    @Autowired
    private CousineRepository cousineRepository;
    
    @Test
    public void whenFindAll_thenReturnCousines() {
    	
    	//given
    	Cousine sushiCousine = new Cousine("Sushi");
    	cousineRepository.save(sushiCousine);
    	
        // when
    	List<Cousine> allCousines = cousineRepository.findAll();
    	
    	// then
    	Assert.notEmpty(allCousines, "No cousine was found"); 
    }
    
    @Test
    public void whenFindById_thenReturnCousine() {
    	
    	final String sushi = "Sushi";
    	
    	//given
    	Cousine sushiCousine = new Cousine(sushi);
    	cousineRepository.save(sushiCousine);
    	
        //when
        Optional<Cousine> cousineound = cousineRepository.findByName(sushi);
        
        //then
        Assert.isTrue(cousineound.isPresent(), "The value must be Sushi");
    }

}
