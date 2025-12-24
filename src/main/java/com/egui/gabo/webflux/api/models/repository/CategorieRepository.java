package com.egui.gabo.webflux.api.models.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.egui.gabo.webflux.api.models.document.Category;

/**
 * Reactive MongoDB repository for Categories entities.
 * <p>
 * Extends {@link ReactiveMongoRepository} to inherit standard CRUD operations
 * (Create, Read, Update, Delete) in a non-blocking, reactive way.
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
public interface CategorieRepository extends ReactiveMongoRepository<Category, String> {

}
