package com.skipthedishes.ut.cousine;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.skipthedishes.dto.CousineDto;
import com.skipthedishes.dto.StoreDto;
import com.skipthedishes.model.Cousine;
import com.skipthedishes.repository.CousineRepository;
import com.skipthedishes.resource.CousineResource;
import com.skipthedishes.service.CousineService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CousineResource.class, secure = false)
@EnableSpringDataWebSupport
public class CousineResourceUT {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private CousineRepository cousineRepository;

	@MockBean
	private CousineService cousineService;
	

	@Test
	public void create() throws Exception {
		CousineDto mockCousine = new CousineDto(1, "Sushi");
		String exampleCousineJson = "{\"name\":\"Sushi\"}";

		when(this.cousineService.save(Mockito.any(CousineDto.class))).thenReturn(mockCousine);

		RequestBuilder requestBuilder = post("/cousine")
				.content(exampleCousineJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);  

		mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andExpect(header().string("Location", "/cousine/3")); 
	}

	@Test
	public void findAll() {
		List<Cousine> listOfCousines = new ArrayList<Cousine>();
		listOfCousines.add(new Cousine(1, "Sushi"));
		listOfCousines.add(new Cousine(2, "Brazilian"));
		listOfCousines.add(new Cousine(3, "Mexican"));

		when(this.cousineService.findAll()).thenReturn(listOfCousines);

		List<Cousine> returned = this.cousineService.findAll();

		assertEquals(listOfCousines, returned);
		assertEquals(listOfCousines.size(), returned.size());
	}
	
	@Test
	public void findByName() throws Exception {
		final String search = "Sushi";
		
		when(this.cousineService.findByName(search)).thenReturn(new CousineDto(1, search));
		
		RequestBuilder requestBuilder = get("/cousine/search/{searchText}", search)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder) 
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("Sushi"))); 
	}
	
	@Test
	public void listStores() throws Exception {
		List<StoreDto> dtos = new ArrayList<>();
		dtos.add(new StoreDto(1, "Sushi Box"));  
		dtos.add(new StoreDto(2, "Japaí"));  
		
		PageRequest pageable = PageRequest.of(0, 20, Sort.unsorted());  
		
		when(this.cousineService.findStores(1, pageable)).thenReturn(dtos);  
		
		RequestBuilder requestBuilder = get("/cousine/{cousineId}/stores", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON); 
		
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().string("[{\"id\":1,\"name\":\"Sushi Box\"},{\"id\":2,\"name\":\"Japaí\"}]"));
	}

}
