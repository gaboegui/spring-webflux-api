package com.egui.gabo.webflux.api.controller;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.egui.gabo.webflux.api.models.document.Product;
import com.egui.gabo.webflux.api.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller for managing Product resources.
 * <p>
 * This class uses the annotation-based programming model (similar to Spring
 * MVC).
 * It communicates with the {@link ProductService} to perform CRUD operations.
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 *
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

	// loads from application.properties
	@Value("${config.upload.path}")
	String uploadDirectory;

	@Autowired
	private ProductService productService;

	/**
	 * Retrieve all products.
	 * <p>
	 * Returns a {@link Flux} which represents a stream of 0 to N elements.
	 * The response body will contain the list of products in JSON format.
	 * </p>
	 * 
	 * @return Mono of ResponseEntity containing the Flux of products
	 */
	@GetMapping
	public Mono<ResponseEntity<Flux<Product>>> listProducts() {

		// ResponseEntity.ok(productService.findAll()));
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productService.findAll()));
	}

	/**
	 * Retrieve a single product by ID.
	 * <p>
	 * Returns a {@link Mono} which represents 0 or 1 element.
	 * If the product is found, it returns 200 OK.
	 * If not found, it returns 404 Not Found (using defaultIfEmpty).
	 * </p>
	 * 
	 * @param id the product ID
	 * @return Mono of ResponseEntity containing the product or not found status
	 */
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Product>> getProduct(@PathVariable String id) {

		return productService.findById(id).map(prod -> ResponseEntity.ok(prod))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/**
	 * Upload a picture for an existing product.
	 * 
	 * @param id   the product ID
	 * @param file the file part from the multipart request
	 * @return Mono of ResponseEntity containing the updated product
	 */
	@PostMapping("/upload/{id}")
	public Mono<ResponseEntity<Product>> uploadProductPicture(@PathVariable String id, @RequestPart FilePart file) {
		return productService.findById(id).flatMap(p -> {
			p.setPicture(UUID.randomUUID().toString()
					.concat(file.filename().replace(" ", "").replace(":", "").replace("\\", "")));
			return file.transferTo(new File(uploadDirectory + p.getPicture())).then(productService.save(p));
		}).map(p -> ResponseEntity.ok(p)).defaultIfEmpty(ResponseEntity.notFound().build());

	}

	/**
	 * Create a product with an image (Form Data).
	 * 
	 * @param product is provided as form-data type
	 * @param file    the image file
	 * @return Mono of ResponseEntity containing the created product
	 */
	@PostMapping("/v1")
	public Mono<ResponseEntity<Product>> saveProductWithPic(Product product, @RequestPart FilePart file) {

		if (product.getCreateAt() == null) {
			product.setCreateAt(new Date());
		}

		product.setPicture(UUID.randomUUID().toString()
				.concat(file.filename().replace(" ", "").replace(":", "").replace("\\", "")));

		return file.transferTo(new File(uploadDirectory + product.getPicture())).then(productService.save(product))
				.map(prod -> ResponseEntity.created(URI.create("/api/products/".concat(prod.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(prod));
	}

	/**
	 * Create a new product (JSON body).
	 * <p>
	 * Validates the request body. If successful, creates the product.
	 * If validation fails, returns a list of error messages with 400 Bad Request.
	 * </p>
	 * 
	 * @param monoProduct is provided as JSON Request Body wrapped in Mono
	 * @return Mono of ResponseEntity containing the created product or error
	 *         details
	 */
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> saveProduct(@Valid @RequestBody Mono<Product> monoProduct) {

		Map<String, Object> response = new HashMap<>();

		return monoProduct.flatMap(product -> {

			if (product.getCreateAt() == null) {
				product.setCreateAt(new Date());
			}

			return productService.save(product).map(prod -> {

				response.put("product", prod);
				response.put("status", HttpStatus.CREATED.value()); // extras
				return ResponseEntity.created(URI.create("/api/products/".concat(prod.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(response);
			});
		})
				.onErrorResume(ex -> {
					return Mono.just(ex).cast(WebExchangeBindException.class)
							.flatMap(er -> Mono.just(er.getFieldErrors()))
							.flatMapMany(errors -> Flux.fromIterable(errors))
							.map(fieldError -> "Field: " + fieldError.getField() + " " + fieldError.getDefaultMessage())
							.collectList()
							.flatMap(list -> {
								response.put("errors", list);
								response.put("status", HttpStatus.BAD_REQUEST.value()); // extras
								return Mono.just(ResponseEntity.badRequest().body(response));
							});
				});

	}

	/**
	 * Update an existing product.
	 * 
	 * @param product the product data from request body
	 * @param id      the product ID from path variable
	 * @return Mono of ResponseEntity containing the updated product
	 */
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Product>> editProduct(@RequestBody Product product, @PathVariable String id) {

		return productService.findById(id).flatMap(p -> {
			p.setName(product.getName());
			p.setPrice(product.getPrice());
			p.setCategory(product.getCategory());

			return productService.save(p);
		}).map(prod -> ResponseEntity.created(URI.create("/api/products/".concat(prod.getId())))
				.contentType(MediaType.APPLICATION_JSON).body(prod)).defaultIfEmpty(ResponseEntity.notFound().build());

	}

	/**
	 * Delete a product by ID.
	 * 
	 * @param id the product ID
	 * @return Mono of ResponseEntity with no content
	 */
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> deleteProduct(@PathVariable String id) {

		return productService.findById(id).flatMap(p -> {
			return productService.delete(p).then(Mono.just(ResponseEntity.noContent().build()));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
