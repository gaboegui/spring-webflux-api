package com.egui.gabo.webflux.api;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.egui.gabo.webflux.api.models.document.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class SpringWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;

	@Test
	void listTest() {
		client.get()
			.uri("/api/v2/products")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()			// start assertions
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Product.class)
			.consumeWith(response -> {
				List<Product> products = response.getResponseBody();
				Assertions.assertTrue(products.size()==8);
			});
			//.hasSize(8);
		
	}

}
