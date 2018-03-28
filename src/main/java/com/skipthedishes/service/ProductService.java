package com.skipthedishes.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skipthedishes.dto.ProductDto;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;
import com.skipthedishes.model.Product;
import com.skipthedishes.repository.ProductRepository;

@Service
@Lazy
public class ProductService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ProductRepository productRepository;
	private ModelMapper modelMapper;
	private MessageSource messageSource;

	@Autowired
	public ProductService(final ProductRepository productRepository, final ModelMapper modelMapper, final MessageSource messageSource) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}

	public Product validateNotFoundProduct(final Integer id) {
		return this.productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException( messageSource.getMessage("product.not.found", new Object[]{id}, LocaleContextHolder.getLocale())));
	}
	
	private void validateDuplicateProduct( final Integer id) {
		if( StringUtils.isEmpty(id)) {
			return;
		}
		
		this.productRepository.findById(id).ifPresent( c -> { throw new DuplicateResourceException( messageSource.getMessage("product.already.exists", new Object[]{id}, LocaleContextHolder.getLocale()) ); });
	}
	
	public void updatePartial( final Integer id, final ProductDto cousineDto){
		this.validateNotFoundProduct(id);
		
		final Optional<Product> cousineFound = this.productRepository.findById( id );
		cousineFound.ifPresent(cousine -> {
				
				final String name = cousineDto.getName();
				if( !StringUtils.isEmpty( name ) ){
					cousine.setName( name );
				}
				
				this.productRepository.save( cousine );
		});
	}
	
	public ProductDto findById(final Integer id) {
		this.validateNotFoundProduct(id); 
		
		Optional<Product> cousineFound = productRepository.findById(id);
		return modelMapper.map(cousineFound.get(), ProductDto.class);
	}
	
	public List<ProductDto> findAllByName( final String searchText, final Pageable pageable ) {
		final Page<Product> products = productRepository.findAllProductsByName(searchText, pageable); 
		Page<ProductDto> map = products.map(c -> this.modelMapper.map(c, ProductDto.class));
		return map.getContent();
	}
	
	public ProductDto save( final ProductDto cousineDto ){
		this.validateDuplicateProduct( cousineDto.getId() ); 
		
		Product cousine = new Product();
		BeanUtils.copyProperties(cousineDto, cousine); 
		
		Product savedProduct = this.productRepository.save(cousine);
		
		return this.modelMapper.map(savedProduct, ProductDto.class);
	}
	
	public List<ProductDto> findAll( final Pageable pageable ){
		final Page<Product> products = this.productRepository.findAll(pageable);
		Page<ProductDto> map = products.map(c -> this.modelMapper.map(c, ProductDto.class));
		return map.getContent();
	} 
	
	public void deleteById( final Integer id ){
		this.validateNotFoundProduct(id);
		this.productRepository.deleteById(id);
	}
	
	public void update( final Integer id, final ProductDto cousineDto ) {
		this.validateNotFoundProduct(id);
		
		final Optional<Product> customerOptinal = this.productRepository.findById( id );

		customerOptinal.ifPresent(cousine -> {
			BeanUtils.copyProperties(cousineDto, cousine, "id");
			this.productRepository.save(cousine);
		});
	}

}
