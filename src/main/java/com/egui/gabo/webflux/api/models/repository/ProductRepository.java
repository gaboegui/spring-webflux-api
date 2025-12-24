package com.egui.gabo.webflux.api.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.egui.gabo.webflux.api.models.document.Product;



/**
 * Reactive MongoDB repository for Product entities.
 * Provides reactive CRUD operations for Product documents.
 * 
 * Methods: 
 * https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html#page-title
 * 
 * JSON Querys:
 * https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.json-based
 * 
 * @author Gabriel Eguiguren P.
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
