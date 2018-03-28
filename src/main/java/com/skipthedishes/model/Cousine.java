package com.skipthedishes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "COUSINE")
public class Cousine implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Cousine() {
	}
	
	public Cousine(final String name) {
		super();
		this.name = name;
	}
	
	public Cousine(final Integer id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, precision = 8, name = "ID")
	private Integer id;
	
	@NotBlank
	private String name;
	
	@OneToMany(mappedBy = "cousine") 
	private List<Store> stores;
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Store> getStores() {
		if( this.stores == null ) {
			stores = new ArrayList<Store>();
		}
		return stores; 
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cousine))
			return false;
		Cousine other = (Cousine) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
