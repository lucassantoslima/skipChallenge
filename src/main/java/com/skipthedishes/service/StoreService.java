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

import com.skipthedishes.dto.StoreDto;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;
import com.skipthedishes.model.Store;
import com.skipthedishes.repository.StoreRepository;

@Service
@Lazy
public class StoreService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private StoreRepository storeRepository;
	private ModelMapper modelMapper;
	private MessageSource messageSource;

	@Autowired
	public StoreService(final StoreRepository storeRepository, final ModelMapper modelMapper, final MessageSource messageSource) {
		this.storeRepository = storeRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}
 
	public Store validateNotFoundStore(final Integer storeId) {
		return this.storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException( messageSource.getMessage("store.not.found", new Object[]{storeId}, LocaleContextHolder.getLocale())));
	}
	
	public boolean exists(final Integer storeId) {
		return this.storeRepository.existsById(storeId);
	}
	
	private void validateDuplicateStore( final Integer id) {
		if( StringUtils.isEmpty(id)) {
			return;
		}
		
		this.storeRepository.findById(id).ifPresent( c -> { throw new DuplicateResourceException( messageSource.getMessage("store.already.exists", new Object[]{id}, LocaleContextHolder.getLocale()) ); });
	}
	
	public void updatePartial( final Integer id, final StoreDto storeDto){
		this.validateNotFoundStore(id);
		
		final Optional<Store> storeFound = this.storeRepository.findById( id );
		storeFound.ifPresent(store -> {
				
				final String name = storeDto.getName();
				if( !StringUtils.isEmpty( name ) ){
					store.setName( name );
				}
				
				this.storeRepository.save( store );
		});
	}
	
	public StoreDto findById(final Integer id) {
		this.validateNotFoundStore(id); 
		
		Optional<Store> storeFound = storeRepository.findById(id);
		return modelMapper.map(storeFound.get(), StoreDto.class);
	}
	
	public StoreDto findByName(final String searchText) {
		Optional<Store> storeFound = storeRepository.findByName(searchText); 
		return modelMapper.map(storeFound.get(), StoreDto.class);
	}
	
	public StoreDto save( final StoreDto storeDto ){
		this.validateDuplicateStore( storeDto.getId() ); 
		
		Store store = new Store();
		BeanUtils.copyProperties(storeDto, store); 
		
		Store savedStore = this.storeRepository.save(store);
		
		return this.modelMapper.map(savedStore, StoreDto.class);
	}
	
	public List<StoreDto> findAll( final Pageable pageable ){
		final Page<Store> stores = this.storeRepository.findAll(pageable);
		Page<StoreDto> map = stores.map(c -> this.modelMapper.map(c, StoreDto.class));
		return map.getContent();
	} 
	
	public List<StoreDto> findAllProducts( final Integer storeId, final Pageable pageable ){
		final Page<Store> stores = this.storeRepository.findAllProducts(storeId, pageable);   
		Page<StoreDto> map = stores.map(c -> this.modelMapper.map(c, StoreDto.class));
		return map.getContent();
	}
	
	public void deleteById( final Integer id ){
		this.validateNotFoundStore(id);
		this.storeRepository.deleteById(id);
	}
	
	public void update( final Integer id, final StoreDto cousineDto ) {
		this.validateNotFoundStore(id);
		
		final Optional<Store> customerOptinal = this.storeRepository.findById( id );

		customerOptinal.ifPresent(cousine -> {
			BeanUtils.copyProperties(cousineDto, cousine, "id");
			this.storeRepository.save(cousine);
		});
	}

}
