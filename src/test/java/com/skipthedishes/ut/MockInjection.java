package com.skipthedishes.ut;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.skipthedishes.model.Cousine;
import com.skipthedishes.repository.CousineRepository;
import com.skipthedishes.service.CousineService;

@RunWith(MockitoJUnitRunner.class)
public class MockInjection {

	@Mock
	CousineRepository dataServiceMock;

	@InjectMocks
	CousineService businessImpl;

	@Test
	public void testFindTheGreatestFromAllData() {
		List<Cousine> list = new ArrayList<>();
		list.add(new Cousine("Sushi"));
		list.add(new Cousine("Brazilian"));
		list.add(new Cousine("Mexican"));
		
		when(dataServiceMock.findAll()).thenReturn( list );
		assertEquals(3, businessImpl.findAll().size());
	}

	
}
