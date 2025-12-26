package com.egui.gabo.webflux.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.egui.gabo.webflux.api.models.document.Category;
import com.egui.gabo.webflux.api.models.document.Product;
import com.egui.gabo.webflux.api.models.repository.CategorieRepository;
import com.egui.gabo.webflux.api.models.repository.ProductRepository;

import reactor.core.publisher.Flux;


/**
 * Main entry point for the Spring Boot WebFlux API application.
 * <p>
 * This class creates the Spring application context and also implements
 * {@link CommandLineRunner}
 * to perform initial data setup when the application starts.
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SpringWebfluxApirestApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringWebfluxApirestApplication.class);

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApirestApplication.class, args);
	}

	/**
	 * Callback used to run the bean.
	 * <p>
	 * This method is executed after the application context is loaded and before
	 * the Spring Application run method completes.
	 * It allows us to reset the database state and populate it with sample data for
	 * development purposes.
	 * </p>
	 * 
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {
		// Clear existing test data from MongoDB collections to start fresh
		mongoTemplate.dropCollection("products").subscribe();
		mongoTemplate.dropCollection("categories").subscribe();

		// Insert test products for development environment
		Category cat1 = new Category("Electronic");
		Category cat2 = new Category("Computers");

		// Using Flux to handle the reactive stream of data insertion
		Flux.just(cat1, cat2)
				.flatMap(categorieRepository::save) // Save categories asynchronously
				.thenMany( // execute a new Flux inmmediatly after categories are saved
						Flux.just(new Product("TV LG 4k 52in", 500.99, cat1),
								new Product("Camara Sony", 500.99, cat1),
								new Product("Apple watch", 200.99, cat1),
								new Product("Laptop Lenovo", 700.99, cat2),
								new Product("Webcam Logitech", 199.99, cat1),
								new Product("Camara Nikon", 500.99, cat1),
								new Product("TV Haisen 4k 52", 600.99, cat1),
								new Product("Laptop Mac Book Pro", 1600.99, cat2))
								.flatMap(product -> {
									product.setCreateAt(new Date());
									return repository.save(product); // Save each product
								}))
				.subscribe(product -> log.info("Inserted: {}",
						product.getName() + " categorie: " + product.getCategory().getName())); // Subscribe to trigger
																								// the flow and log
																								// results

	}

}
