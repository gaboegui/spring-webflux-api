package com.egui.gabo.webflux.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.egui.gabo.webflux.api.models.document.Category;
import com.egui.gabo.webflux.api.models.document.Product;
import com.egui.gabo.webflux.api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Unit tests for @ProductController
 *  
 * @author Gabriel Eguiguren P.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class ProductControllerUnitTests {
	
	private String URL_ENDPOINT  = "/api/products";
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductService service;

	@Test
	void listTest() {
		client.get()
			.uri(URL_ENDPOINT)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()						
			.expectStatus().isOk()			// start assertions
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Product.class)
			.consumeWith(response -> {
				List<Product> products = response.getResponseBody();
				Assertions.assertTrue(products.size() > 0);
			});
			//.hasSize(8);
		
	}
	
	@Test
	void detailTest() {
		
		Product product = service.findByName("Camara Nikon").block();
		
		
		client.get()
			.uri(URL_ENDPOINT.concat("/{id}"), Collections.singletonMap("id", product.getId()))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()			// start assertions
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("Camara Nikon");
			
	}
	
	@Test
	void createTest() {
		
		Category catDb = service.findCategoryByName("Computers").block();
		
		Product newProduct = new Product("Laptop Acer", 500.00, catDb);
		
		client.post()
		.uri(URL_ENDPOINT)
		.contentType(MediaType.APPLICATION_JSON)   		// request
		.accept(MediaType.APPLICATION_JSON)				// response	
		.body(Mono.just(newProduct), Product.class)
		.exchange()										// call the WS
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()  							// returns a JSON map 
		.jsonPath("$.product.id").isNotEmpty()
		.jsonPath("$.product.name").isEqualTo("Laptop Acer")
		.jsonPath("$.product.price").isEqualTo(500.00);
		
	}
	
	
	@Test
	void createTestBodyHashMap() {
		
		Category catDb = service.findCategoryByName("Computers").block();
		
		Product newProduct = new Product("Laptop Acer", 500.00, catDb);
		
		client.post()
		.uri(URL_ENDPOINT)
		.contentType(MediaType.APPLICATION_JSON)   		// request
		.accept(MediaType.APPLICATION_JSON)				// response	
		.body(Mono.just(newProduct), Product.class)
		.exchange()										// call the WS
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {}) // returns a JSON map 
		.consumeWith(response -> {
			Object o = response.getResponseBody().get("product");
			Product product = new ObjectMapper().convertValue(o, Product.class);
			Assertions.assertTrue(!product.getId().isEmpty());
			Assertions.assertTrue(product.getName().equals("Laptop Acer"));
			Assertions.assertTrue(product.getPrice().equals(500.00));
		});  							
		
	}
	
	@Test
	void editTest() {
		
		Product productDb = service.findByName("Camara Nikon").block();
		Category catDb = service.findCategoryByName("Electronic").block();
		
		Product editedProduct = new Product("Camara Minolta", 750.00, catDb);
		
		
		client.put()
			.uri(URL_ENDPOINT.concat("/{id}"), Collections.singletonMap("id", productDb.getId()))
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(editedProduct), Product.class)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Product.class)
			.consumeWith(response -> {
				Product product = response.getResponseBody();
				Assertions.assertTrue(!product.getId().isEmpty());
				Assertions.assertTrue(product.getName().equals("Camara Minolta"));
				Assertions.assertTrue(product.getPrice().equals(750.00));
			});
			
	}
	
	@Test
	void deleteTest() {
		
		Product productDb = service.findByName("Apple watch").block();

		client.delete()
			.uri(URL_ENDPOINT.concat("/{id}"), Collections.singletonMap("id", productDb.getId()))
			.exchange()
			.expectStatus().isNoContent()  //204
			.expectBody().isEmpty(); 
		
		client.get()
			.uri(URL_ENDPOINT.concat("/{id}"), Collections.singletonMap("id", productDb.getId()))
			.exchange()
			.expectStatus().isNotFound();
	}

}
