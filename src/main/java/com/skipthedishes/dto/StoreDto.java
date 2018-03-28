package com.skipthedishes.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;

	public StoreDto() {
	}
	
	public StoreDto(final Integer id) {
		super();
		this.id = id;
	}

	public StoreDto(final Integer storeId, final String name) {
		super();
		this.id = storeId;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
