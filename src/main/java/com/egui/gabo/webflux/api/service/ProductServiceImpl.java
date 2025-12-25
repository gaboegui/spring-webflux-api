package com.egui.gabo.webflux.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egui.gabo.webflux.api.models.document.Category;
import com.egui.gabo.webflux.api.models.document.Product;
import com.egui.gabo.webflux.api.models.repository.CategorieRepository;
import com.egui.gabo.webflux.api.models.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of ProductService using ReactiveMongoRepository.
 * <p>
 * This class interacts with the MongoDB repositories to perform database
 * operations.
 * It is annotated with @Service to be discovered by Spring's component
 * scanning.
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productDao;

	@Autowired
	private CategorieRepository categorieDao;

	@Override
	public Flux<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public Flux<Product> findAllNameUppercase() {
		return productDao.findAll()
				.map(product -> {
					product.setName(product.getName().toUpperCase());
					return product;
				});
	}

	@Override
	public Mono<Product> findById(String id) {
		return productDao.findById(id);
	}

	@Override
	public Mono<Product> save(Product p) {
		return productDao.save(p);
	}

	@Override
	public Mono<Void> delete(Product p) {
		return productDao.delete(p);
	}

	@Override
	public Flux<Category> findAllCategories() {

		return categorieDao.findAll();
	}

	@Override
	public Mono<Category> findCategoryById(String id) {

		return categorieDao.findById(id);
	}

	@Override
	public Mono<Category> saveCategory(Category c) {

		return categorieDao.save(c);
	}

	@Override
	public Mono<Product> findByName(String name) {
		
		return productDao.findByName(name);
	}

	@Override
	public Mono<Category> findCategoryByName(String name) {
		
		return categorieDao.findByName(name);
	}

}
