package com.skipthedishes.ut.cousine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skipthedishes.dto.ProductDto;
import com.skipthedishes.repository.ProductRepository;
import com.skipthedishes.resource.ProductResource;
import com.skipthedishes.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductResource.class, secure = false)
@EnableSpringDataWebSupport
public class ProductResourceUT {
	
	private static final String PRODUCT_NAME = "Spicy Shrimp & Mango";
	private static final String PRODUCT_DESCRIPTION = "Shrimp, kani kama, mango, avocado, lettuce, red masago, green onion, spicy sauce, spicy light mayo";
	private static final double PRICE = 11.95;
	
	private static final String URL_REQUEST = "/product";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Mock
	private ProductRepository productRepository;

	@MockBean
	private ProductService productService;
	
	@Test
	public void create() throws Exception {
		final ProductDto mockProduct = new ProductDto(1, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE);
		
		final String jsonToInsert = objectMapper.writeValueAsString(new ProductDto(PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE));

		when(this.productService.save(Mockito.any(ProductDto.class))).thenReturn(mockProduct); 

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_REQUEST) 
				.content(jsonToInsert) 
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		assertEquals(HttpStatus.CREATED.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void findAll() {
		List<ProductDto> listOfProducts = new ArrayList<ProductDto>(); 
		listOfProducts.add(new ProductDto(1, "Chicken & Mango Poke", "chicken, mango, tempura, avocado, cucumber, lettuce, green onion", 12.95));
		listOfProducts.add(new ProductDto(2, "Salmon Tacos", "salmon, avocado, lettuce, coriander, ponzu sauce, roasted garlic", 6.95));
		listOfProducts.add(new ProductDto(3, "Chocolate-Banana Bomb", "a delicious mixture of banana and melted chocolate in a crispy coating", 3.50));
		
		PageRequest pageable = PageRequest.of(0, 20, Sort.unsorted()); 

		when(this.productService.findAll(pageable)).thenReturn(listOfProducts); 

		List<ProductDto> returned = this.productService.findAll(pageable); 

		assertEquals(listOfProducts, returned);
		assertEquals(listOfProducts.size(), returned.size()); 
	}
	
	@Test
	public void findAllByName() throws Exception {
		final String search = "Chicken";	
		
		List<ProductDto> listOfProducts = new ArrayList<ProductDto>(); 
		listOfProducts.add(new ProductDto(1, "Chicken & Mango Poke", "chicken, mango, tempura, avocado, cucumber, lettuce, green onion", 12.95));
		listOfProducts.add(new ProductDto(2, "Chicken Tacos", "salmon, avocado, lettuce, coriander, ponzu sauce, roasted garlic", 6.95));
		
		PageRequest pageable = PageRequest.of(0, 20, Sort.unsorted()); 
		
		when(this.productService.findAllByName(search, pageable)).thenReturn(listOfProducts);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_REQUEST+"/search/{searchText}", search) 
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		final String jsonResult = objectMapper.writeValueAsString(listOfProducts); 
		
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().string(jsonResult));  
	}

}
