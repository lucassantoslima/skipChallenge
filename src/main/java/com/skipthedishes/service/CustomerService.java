package com.skipthedishes.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.skipthedishes.dto.CustomerDto;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;
import com.skipthedishes.model.Customer;
import com.skipthedishes.repository.CustomerRepository;
import com.skipthedishes.utils.SystemCostant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Lazy
public class CustomerService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private CustomerRepository customerRepository;
	private ModelMapper modelMapper;
	private MessageSource messageSource;

	@Autowired
	public CustomerService(final CustomerRepository customerRepository, final ModelMapper modelMapper, final MessageSource messageSource) {
		this.customerRepository = customerRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}
	
	private void validateDuplicateCustomer( final String email) {
		this.customerRepository.findByEmail(email).ifPresent( c -> { throw new DuplicateResourceException( messageSource.getMessage("customer.already.exists", new Object[]{email}, LocaleContextHolder.getLocale()) ); });
	}
	
	public Customer validateNotFoundCustomer(final Integer custumerId) {
		return this.customerRepository.findById(custumerId).orElseThrow(() -> new ResourceNotFoundException( messageSource.getMessage("customer.not.found", new Object[]{custumerId}, LocaleContextHolder.getLocale())));
	}
	
	public Customer validateNotFoundCustomer(final String email) {
		return this.customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException( messageSource.getMessage("customer.not.found.email", new Object[]{email}, LocaleContextHolder.getLocale())));
	}
	
	public String createToken( final CustomerDto customerDto) {
		
		final String jwsToken = Jwts.builder()
                .setSubject(customerDto.getEmail())  
                .claim("email", customerDto.getEmail())
                .claim("password", customerDto.getPassword())
                .setExpiration(new Date(System.currentTimeMillis() + 1296000000L)) 
                .signWith(SignatureAlgorithm.HS512, SystemCostant.SECRET_KEY)
                .compact(); 
		 
		return jwsToken;
		
	}
	
	public String save( final CustomerDto customerDto ){
		this.validateDuplicateCustomer( customerDto.getEmail() ); 
		
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDto, customer); 
		
		this.customerRepository.save(customer);
		
		return createToken(customerDto); 
	}
	
	public CustomerDto findByEmail(final String email) {
		Optional<Customer> customer = this.customerRepository.findByEmail(email);
		return this.modelMapper.map(customer.get(), CustomerDto.class);
	}

}
