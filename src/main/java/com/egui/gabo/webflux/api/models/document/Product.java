package com.egui.gabo.webflux.api.models.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Product entity/document for MongoDB.
 * Represents a product with name, price, and creation timestamp.
 * 
 * @author Gabriel Eguiguren P.
 */
@Document(collection = "products")
public class Product {
	
	@Id
	private String id;
	
	@NotEmpty
	private String name;
	
	@NotNull	
	private Double price;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	@Valid
	@NotNull
	private Category category;
	
	private String picture;
	
	/** Default constructor. */
	public Product() {
	}

	/**
	 * Constructor with name and price.
	 * 
	 * @param name Product name
	 * @param price Product price
	 */
	public Product(String name, Double price) {
		this.name = name;
		this.price = price;
	}
	
	public Product(String name, Double price, Category cat) {
		this(name,price);
		this.category = cat;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
