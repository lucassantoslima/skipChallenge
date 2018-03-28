package com.skipthedishes.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "STORE")
public class Store implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false, precision = 8, name = "ID")
	private Integer id;

	@NotBlank
	private String name;
	
	@ManyToOne
	@JoinColumn(name="cousine_id")
	private Cousine cousine;

	@OneToMany(mappedBy = "store", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
	private Set<Product> products;
	
	public Store() {
	}
	
	public Store(Integer storeId, @NotBlank String name) {
		super();
		this.id = storeId;
		this.name = name;
	}

	public Store(Integer id, @NotBlank String name, Cousine cousine) {
		super();
		this.id = id;
		this.name = name;
		this.cousine = cousine;
	}

	public Integer getId() {
		return id;
	}

	public Set<Product> getProducts() {
		if( products == null ) {
			products = new HashSet<Product>();
		}
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Optional<Cousine> getCousine() { 
		return Optional.ofNullable(cousine);
	}

	public void setCousine(Cousine cousine) {
		this.cousine = cousine;
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
		if (!(obj instanceof Store))
			return false;
		Store other = (Store) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
