package com.egui.gabo.webflux.api.models.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.egui.gabo.webflux.api.models.document.Product;

import reactor.core.publisher.Mono;

/**
 * Reactive MongoDB repository for Product entities.
 * <p>
 * Provides reactive CRUD operations for Product documents.
 * By extending ReactiveMongoRepository, we get methods like save(), deleted(),
 * findById(), and findAll() out of the box.
 * </p>
 * 
 * <p>
 * Useful References:
 * <ul>
 * <li><a href=
 * "https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html#page-title">Query
 * Methods</a></li>
 * <li><a href=
 * "https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.json-based">JSON
 * Queries</a></li>
 * </ul>
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

	Mono<Product> findByName(String name);
	
	// alternative using native mongo query
	@Query("{'name':?0}") 		
	Mono<Product> lookByName(String name);

}
