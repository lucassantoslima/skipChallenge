package com.skipthedishes.resource;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skipthedishes.dto.CustomerDto;
import com.skipthedishes.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private final CustomerService customerService;

	@Autowired
	public CustomerResource( final CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@PostMapping
	public ResponseEntity<String> create(@RequestBody final CustomerDto customerDto, final HttpServletResponse response) {
		String token = this.customerService.save(customerDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(token); 
	} 
	
	@PostMapping("/auth")
	public ResponseEntity<String> oauth(@RequestParam final String email, @RequestParam final String password){
		String token = this.customerService.createToken(new CustomerDto(email, password)); 
		return ResponseEntity.status(HttpStatus.CREATED).body(token);  
	}
	
}
