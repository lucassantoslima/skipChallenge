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

import com.skipthedishes.dto.ProductDto;
import com.skipthedishes.events.CreatedResourceEvent;
import com.skipthedishes.model.Cousine;
import com.skipthedishes.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ProductService productService;

	private ApplicationEventPublisher publisher;

	@Autowired
	public ProductResource( final ProductService productService, final ApplicationEventPublisher publisher) {
		this.productService = productService;
		this.publisher = publisher;
	}
	
	@GetMapping
	public ResponseEntity<List<ProductDto>> listAll(final Pageable pageable) {
		return ResponseEntity.ok(this.productService.findAll(pageable)); 
	}
	
	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody final ProductDto productDto, final HttpServletResponse response) {
		ProductDto cousineSaved = this.productService.save(productDto);
		publisher.publishEvent(new CreatedResourceEvent(this, response, cousineSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(cousineSaved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cousine> update(@PathVariable("id") final Integer id,	@RequestBody final ProductDto cousineDto ) {
		this.productService.update(id, cousineDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") final Integer id) {
		this.productService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePartial(@PathVariable("id") @NotNull final Integer id, @RequestBody final ProductDto cousineDto) {
		this.productService.updatePartial(id, cousineDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findById(@PathVariable("id") Integer id) {
		final ProductDto cousineFound = this.productService.findById(id);	
		return new ResponseEntity<ProductDto>(cousineFound, HttpStatus.OK);
	}
	
	@GetMapping("/search/{searchText}") 
	public ResponseEntity<List<ProductDto>> findallByName(@PathVariable("searchText") String searchText, final Pageable pageable) {
		return ResponseEntity.ok(this.productService.findAllByName(searchText, pageable));   
	}
}
