package com.skipthedishes.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CousineDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private List<StoreDto> stores = new ArrayList<StoreDto>();
	
	public CousineDto() {
	}
	
	public CousineDto(final Integer cousineId, final String name) {
		super();
		this.id = cousineId;
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

	public List<StoreDto> getStores() {
		if( stores == null ) {
			this.stores = new ArrayList<StoreDto>(); 
		}
		return stores;
	}

	public void setStores(List<StoreDto> stores) {
		this.stores = stores;
	}
	
	public void addStores(final StoreDto storeDto) {
		this.stores.add(storeDto);
	}

}
