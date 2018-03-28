package com.skipthedishes.ut;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import com.skipthedishes.dto.CousineDto;
import com.skipthedishes.model.Cousine;
import com.skipthedishes.repository.CousineRepository;
import com.skipthedishes.service.CousineService;


public class CousineUTM {
	
	private CousineRepository cousineRepositoryMock;
	
	private ModelMapper modelMapper;

    private CousineService cousineService;
	
	@Before
	public void before() {
		cousineRepositoryMock = mock(CousineRepository.class);
		modelMapper = mock(ModelMapper.class);
		cousineService = new CousineService(cousineRepositoryMock, modelMapper );
	}
	
	/*@Test
	public void teste2() {
		CousineService serviceMock = mock(CousineService.class);
		when(serviceMock.findByName("Sushi")).thenReturn(new CousineDto(1, "Sushi")); 
		
		SomeBusinessImpl businessImpl = new SomeBusinessImpl(serviceMock);
		int result = businessImpl.findTheGreatestFromAllData();
		assertEquals(24, result);
	}*/
	
	@Test
    public void create() {
		
		CousineDto sushi = new CousineDto(null, "Sushi");
        
        CousineDto returned  = cousineService.save(sushi); 

        ArgumentCaptor<Cousine> personArgument = ArgumentCaptor.forClass(Cousine.class);
        verify(cousineRepositoryMock, times(1)).save(personArgument.capture());
        verifyNoMoreInteractions(cousineRepositoryMock);

        assertEquals(sushi, returned);
    }

}
