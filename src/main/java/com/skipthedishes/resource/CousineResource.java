package com.skipthedishes.resource;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
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

import com.skipthedishes.dto.CousineDto;
import com.skipthedishes.dto.StoreDto;
import com.skipthedishes.events.CreatedResourceEvent;
import com.skipthedishes.service.CousineService;

@RestController
@RequestMapping("/cousine")
public class CousineResource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final CousineService cousineService;
	
	private ApplicationEventPublisher publisher;
	
	@Autowired
	public CousineResource( final CousineService cousineService, final ApplicationEventPublisher publisher) {
		this.cousineService = cousineService;
		this.publisher = publisher;
	}

	@GetMapping
	public Page<CousineDto> listAll(final Pageable pageable) { 
		return this.cousineService.findAll(pageable);
	}
	
	@GetMapping("/{cousineId}/stores")
	public List<StoreDto> listStores(@PathVariable("cousineId") final Integer cousineId, final Pageable pageable) {
		return this.cousineService.findStores(cousineId, pageable); 
	}

	@PostMapping
	public ResponseEntity<CousineDto> create(@RequestBody final CousineDto cousineDto, final HttpServletResponse response) {
		CousineDto cousineSaved = this.cousineService.save(cousineDto);
		publisher.publishEvent(new CreatedResourceEvent(this, response, cousineSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(cousineSaved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CousineDto> update(@PathVariable("id") final Integer id,	@RequestBody final CousineDto cousineDto ) {
		CousineDto cousineUpdated = this.cousineService.update(id, cousineDto);
		return new ResponseEntity<CousineDto>(cousineUpdated, HttpStatus.OK); 
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") final Integer id) {
		this.cousineService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePartial(@PathVariable("id") @NotNull final Integer id, @RequestBody final CousineDto cousineDto) {
		this.cousineService.updatePartial(id, cousineDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CousineDto> findById(@PathVariable("id") Integer id) {
		final CousineDto cousineFound = this.cousineService.findById(id);	
		return new ResponseEntity<CousineDto>(cousineFound, HttpStatus.OK);
	}
	
	@GetMapping("/search/{searchText}") 
	public ResponseEntity<CousineDto> findByName(@PathVariable("searchText") String searchText) {
		final CousineDto cousineFound = this.cousineService.findByName(searchText);	
		return new ResponseEntity<CousineDto>(cousineFound, HttpStatus.OK);
	}

}
