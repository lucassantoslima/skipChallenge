package com.skipthedishes.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.skipthedishes.model.Customer;
import com.skipthedishes.repository.CustomerRepository;

@Component
@Lazy
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<Customer> customerOptional = customerRepository.findByEmail(email);
		
		Customer customerFound = customerOptional.orElseThrow(() -> new UsernameNotFoundException("Wrong Email and/or password"));
		
		final List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");

		final String encode = new BCryptPasswordEncoder().encode(customerFound.getPassword());
		return new User(customerFound.getEmail(), encode, authorityListUser);  
	}

}
