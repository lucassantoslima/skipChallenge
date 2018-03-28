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

import com.skipthedishes.dto.CousineDto;
import com.skipthedishes.dto.StoreDto;
import com.skipthedishes.exceptions.DuplicateResourceException;
import com.skipthedishes.exceptions.ResourceNotFoundException;
import com.skipthedishes.model.Cousine;
import com.skipthedishes.model.Store;
import com.skipthedishes.repository.CousineRepository;

@Service
@Lazy
public class CousineService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private CousineRepository cousineRepository;
	private ModelMapper modelMapper;
	private MessageSource messageSource;

	@Autowired
	public CousineService(final CousineRepository cousineRepository, final ModelMapper modelMapper) {
		this.cousineRepository = cousineRepository;
		this.modelMapper = modelMapper;
	}

	private void validateNotFoundCousine(final Integer id) {
		this.cousineRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}
	
	private void validateDuplicateCousine( final Integer id) {
		if( StringUtils.isEmpty(id)) {
			return;
		}
		this.cousineRepository.findById(id).ifPresent( c -> { throw new DuplicateResourceException(messageSource.getMessage("cousine.not.found", new Object[]{id}, LocaleContextHolder.getLocale())); });
	}
	
	public void updatePartial( final Integer id, final CousineDto cousineDto){
		this.validateNotFoundCousine(id);
		
		final Optional<Cousine> cousineFound = this.cousineRepository.findById( id );
		cousineFound.ifPresent(cousine -> {
				
				final String name = cousineDto.getName();
				if( !StringUtils.isEmpty( name ) ){
					cousine.setName( name );
				}
				
				this.cousineRepository.save( cousine );
		});
	}
	
	public CousineDto findById(final Integer id) {
		this.validateNotFoundCousine(id); 
		
		Optional<Cousine> cousineFound = cousineRepository.findById(id);
		return modelMapper.map(cousineFound.get(), CousineDto.class);
	}
	
	public CousineDto findByName(final String searchText) {
		Optional<Cousine> cousineFound = cousineRepository.findByName(searchText); 
		return modelMapper.map(cousineFound.get(), CousineDto.class);
	}
	
	public CousineDto save( final CousineDto cousineDto ){
		this.validateDuplicateCousine( cousineDto.getId() ); 
		
		Cousine cousine = new Cousine();
		BeanUtils.copyProperties(cousineDto, cousine); 
		
		Cousine savedCousine = this.cousineRepository.save(cousine);
		
		return this.modelMapper.map(savedCousine, CousineDto.class);
	}
	 
	public Page<CousineDto> findAll( final Pageable pageable ){
		final Page<Cousine> cousines = this.cousineRepository.findAll(pageable);
		return cousines.map(c -> this.modelMapper.map(c, CousineDto.class));
	} 
	
	public List<Cousine> findAll(){ 
		return this.cousineRepository.findAll();
	} 
	
	public List<StoreDto> findStores( final Integer cousineId, final Pageable pageable ){
		final Page<Store> stores = this.cousineRepository.findStores(cousineId, pageable);  
		Page<StoreDto> map = stores.map(c -> this.modelMapper.map(c, StoreDto.class));
		return map.getContent();
	}
	
	public void deleteById( final Integer id ){
		this.validateNotFoundCousine(id);
		this.cousineRepository.deleteById(id);
	}
	
	public CousineDto update( final Integer id, final CousineDto cousineDto ) {
		this.validateNotFoundCousine(id);
		
		final Optional<Cousine> customerOptinal = this.cousineRepository.findById( id );

		if( customerOptinal.isPresent() ) {
			Cousine cousine = customerOptinal.get();
			BeanUtils.copyProperties(cousineDto, cousine, "id");
			Cousine objSaved = this.cousineRepository.save(cousine);
			return this.modelMapper.map(objSaved, CousineDto.class);
		}
		
		return null;
	}

}
