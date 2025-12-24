package com.egui.gabo.webflux.api.handler;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.egui.gabo.webflux.api.models.document.Category;
import com.egui.gabo.webflux.api.models.document.Product;
import com.egui.gabo.webflux.api.service.ProductService;

import org.springframework.validation.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Handler class for Functional WebFlux Endpoints.
 * <p>
 * This class contains the logic to handle HTTP requests routed via
 * {@link com.egui.gabo.webflux.api.RouterFunctionConfig}.
 * It operates on {@link ServerRequest} and returns a {@link Mono} of
 * {@link ServerResponse}.
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
@Component
public class ProductHandler {

	// loads from application.properties
	@Value("${config.upload.path}")
	String uploadDirectory;

	@Autowired
	private ProductService service;

	@Autowired
	private Validator validator;

	/**
	 * Lists all products.
	 * 
	 * @param request the incoming server request
	 * @return a ServerResponse containing all products
	 */
	public Mono<ServerResponse> listProduct(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll(), Product.class);
	}

	/**
	 * Gets a single product by ID.
	 * 
	 * @param request the incoming server request containing path variable 'id'
	 * @return a ServerResponse containing the product or 404 Not Found
	 */
	public Mono<ServerResponse> seeProduct(ServerRequest request) {
		String id = request.pathVariable("id");

		return service.findById(id).flatMap(prod -> ServerResponse.ok().bodyValue(prod))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	/**
	 * Creates a new product.
	 * <p>
	 * This method performs validation on the product object.
	 * If invalid, it returns a 400 Bad Request with error details.
	 * If valid, it saves the product and returns 201 Created.
	 * </p>
	 * 
	 * @param request the incoming server request containing the product JSON
	 * @return a ServerResponse indicative of the outcome
	 */
	public Mono<ServerResponse> createProduct(ServerRequest request) {

		Mono<Product> product = request.bodyToMono(Product.class);

		return product.flatMap(p -> {

			Errors errors = new BeanPropertyBindingResult(p, Product.class.getName());
			validator.validate(p, errors);

			if (errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError -> "The field " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list -> ServerResponse.badRequest().bodyValue(list));
			} else {
				if (p.getCreateAt() == null) {
					p.setCreateAt(new Date());
				}

				return service.save(p).flatMap(pdb -> ServerResponse
						.created(URI.create("/api/v2/products/".concat(pdb.getId()))).bodyValue(pdb));
			}
		});
	}

	/**
	 * Updates an existing product.
	 * 
	 * @param request the incoming server request containing path variable 'id' and
	 *                product JSON
	 * @return a ServerResponse with the updated product
	 */
	public Mono<ServerResponse> updateProduct(ServerRequest request) {

		Mono<Product> product = request.bodyToMono(Product.class);
		String id = request.pathVariable("id");

		Mono<Product> productDb = service.findById(id);

		return productDb.zipWith(product, (prodDb, prodReq) -> {
			prodDb.setName(prodReq.getName());
			prodDb.setPrice(prodReq.getPrice());
			prodDb.setCategory(prodReq.getCategory());
			return prodDb;
		}).flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId())))
				.body(service.save(p), Product.class).switchIfEmpty(ServerResponse.notFound().build()));
	}

	/**
	 * Creates a new product including an initial image upload.
	 * 
	 * @param request the incoming server request (multipart)
	 * @return a ServerResponse with the created product
	 */
	public Mono<ServerResponse> createProductWithImage(ServerRequest request) {

		// get the product values from form-request
		Mono<Product> productMono = request.multipartData().map(multipart -> {
			FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
			FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
			FormFieldPart categoryId = (FormFieldPart) multipart.toSingleValueMap().get("category.id");
			FormFieldPart categoryName = (FormFieldPart) multipart.toSingleValueMap().get("category.name");

			Category cat = new Category(categoryName.value());
			cat.setId(categoryId.value());
			return new Product(name.value(), Double.parseDouble(price.value()), cat);
		});

		// the the file from form and transfer to upload directory
		return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file")).cast(FilePart.class)
				.flatMap(file -> productMono.flatMap(prod -> {
					prod.setPicture(UUID.randomUUID().toString()
							.concat(file.filename().replace(" ", "").replace(":", "").replace("\\", "")));
					prod.setCreateAt(new Date());

					return file.transferTo(new File(uploadDirectory + prod.getPicture())).then(service.save(prod));
				}))
				.flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId()))).bodyValue(p));
	}

	/**
	 * Uploads an image to an existing product.
	 * 
	 * @param request the incoming server request containing 'id' and file
	 * @return a ServerResponse with the updated product
	 */
	public Mono<ServerResponse> uploadImage(ServerRequest request) {

		String id = request.pathVariable("id");

		return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file")).cast(FilePart.class)
				.flatMap(file -> service.findById(id).flatMap(prod -> {
					prod.setPicture(UUID.randomUUID().toString()
							.concat(file.filename().replace(" ", "").replace(":", "").replace("\\", "")));
					return file.transferTo(new File(uploadDirectory + prod.getPicture())).then(service.save(prod));
				})).flatMap(p -> ServerResponse.created(URI.create("/api/v2/products/".concat(p.getId()))).bodyValue(p))
				.switchIfEmpty(ServerResponse.notFound().build());

	}

	/**
	 * Deletes a product.
	 * 
	 * @param request the incoming server request containing 'id'
	 * @return a ServerResponse with no content
	 */
	public Mono<ServerResponse> deleteProduct(ServerRequest request) {

		String id = request.pathVariable("id");
		Mono<Product> productDb = service.findById(id);

		return productDb.flatMap(p -> service.delete(p).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

}
