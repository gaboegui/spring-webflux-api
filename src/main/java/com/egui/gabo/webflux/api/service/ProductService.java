package com.egui.gabo.webflux.api.service;

import com.egui.gabo.webflux.api.models.document.Category;
import com.egui.gabo.webflux.api.models.document.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing Products and Categories.
 * Defines operations to be implemented by the service layer.
 * 
 * @author Gabriel Eguiguren P.
 */
public interface ProductService {

	/**
	 * Retrieve all products.
	 * 
	 * @return a Flux of all products
	 */
	public Flux<Product> findAll();

	/**
	 * Retrieve all products with their names converted to uppercase.
	 * 
	 * @return a Flux of products with uppercase names
	 */
	public Flux<Product> findAllNameUppercase();

	/**
	 * Find a product by its ID.
	 * 
	 * @param id the product ID
	 * @return a Mono containing the product if found, or empty
	 */
	public Mono<Product> findById(String id);
	
	/**
	 * Find a product by its name.
	 * 
	 * @param name the product Name
	 * @return a Mono containing the product if found, or empty
	 */
	public Mono<Product> findByName(String name);
	

	/**
	 * Save a product.
	 * 
	 * @param p the product to save
	 * @return a Mono containing the saved product
	 */
	public Mono<Product> save(Product p);

	/**
	 * Delete a product.
	 * 
	 * @param p the product to delete
	 * @return a Mono<Void> that completes when the deletion is finished
	 */
	public Mono<Void> delete(Product p);

	/* Simplify calls in Controller */
	/**
	 * Retrieve all categories.
	 * 
	 * @return a Flux of all categories
	 */
	public Flux<Category> findAllCategories();

	/**
	 * Find a category by its ID.
	 * 
	 * @param id the category ID
	 * @return a Mono containing the category if found
	 */
	public Mono<Category> findCategoryById(String id);
	
	/**
	 * Find a category by its name.
	 * 
	 * @param name the category name to search
	 * @return a Mono containing the category if found
	 */
	public Mono<Category> findCategoryByName(String name);
	

	/**
	 * Save a category.
	 * 
	 * @param c the category to save
	 * @return a Mono containing the saved category
	 */
	public Mono<Category> saveCategory(Category c);

}
