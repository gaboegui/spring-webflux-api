package com.egui.gabo.webflux.api.models.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;

/**
 * Represents a product category.
 * 
 * @author Gabriel Eguiguren P.
 */
@Document(collection = "categories")
public class Category {

	@Id
	@NotEmpty
	private String id;

	@NotEmpty
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Constructor with name.
	 * 
	 * @param name Category name
	 */
	public Category(@NotEmpty String name) {
		this.name = name;
	}

	/** Default constructor. */
	public Category() {
	}

}
