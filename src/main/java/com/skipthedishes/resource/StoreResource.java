package com.skipthedishes.resource;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skipthedishes.dto.StoreDto;
import com.skipthedishes.events.CreatedResourceEvent;
import com.skipthedishes.model.Store;
import com.skipthedishes.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreResource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final StoreService storeService;
	
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public StoreResource( final StoreService storeService, final ApplicationEventPublisher publisher) {
		this.storeService = storeService;
		this.publisher = publisher;
	}

	@GetMapping
	public List<StoreDto> listAll(final Pageable pageable) {
		return this.storeService.findAll(pageable);
	}
	
	@GetMapping("/{storeId}/products")
	public List<StoreDto> listProducts(@PathVariable("storeId") final Integer storeId, final Pageable pageable) {
		return this.storeService.findAllProducts(storeId, pageable); 
	}

	@PostMapping
	public ResponseEntity<StoreDto> create(@RequestBody final StoreDto storeDto, final HttpServletResponse response) {
		StoreDto storeSaved = this.storeService.save(storeDto);
		publisher.publishEvent(new CreatedResourceEvent(this, response, storeSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(storeSaved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Store> update(@PathVariable("id") final Integer id, @RequestBody final StoreDto storeDto ) {
		this.storeService.update(id, storeDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") final Integer id) {
		this.storeService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePartial(@PathVariable("id") @NotNull final Integer id, @RequestBody final StoreDto storeDto) {
		this.storeService.updatePartial(id, storeDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoreDto> findById(@PathVariable("id") Integer id) {
		final StoreDto storeFound = this.storeService.findById(id);	
		return new ResponseEntity<StoreDto>(storeFound, HttpStatus.OK);
	}
	
	@GetMapping("/search/{searchText}") 
	public ResponseEntity<StoreDto> findById(@PathVariable("searchText") String searchText) {
		final StoreDto storeFound = this.storeService.findByName(searchText);	
		return new ResponseEntity<StoreDto>(storeFound, HttpStatus.OK);
	}

}
