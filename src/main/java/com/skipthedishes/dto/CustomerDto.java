package com.skipthedishes.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class CustomerDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String name;
	private String address;
	private LocalDateTime creation;
	private String password;
	private String token;

	public CustomerDto() {
	}

	public CustomerDto(Integer id) {
		super();
		this.id = id;
	}
	
	

	public CustomerDto(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public CustomerDto(String email, String name, String address, LocalDateTime creation, String password) {
		super();
		this.email = email;
		this.name = name;
		this.address = address;
		this.creation = creation;
		this.password = password;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreation() {
		return creation;
	}

	public void setCreation(LocalDateTime creation) {
		this.creation = creation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
