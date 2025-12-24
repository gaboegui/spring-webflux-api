package com.egui.gabo.webflux.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.egui.gabo.webflux.api.handler.ProductHandler;

/**
 * Configuration class for Functional WebFlux Endpoints.
 * <p>
 * Unlike the annotation-based programming model (using @RestController
 * and @RequestMapping),
 * the functional model uses {@link RouterFunction} to route requests to handler
 * functions.
 * check more in:
 * https://docs.spring.io/spring-framework/reference/web/webflux-functional.html
 * </p>
 * 
 * @author Gabriel Eguiguren P.
 */
@Configuration
public class RouterFunctionConfig {

	/**
	 * Defines the routes for the application using the functional programming
	 * model.
	 * <p>
	 * This bean acts as the central routing configuration. It maps incoming HTTP
	 * requests
	 * (defined by predicates like {@code GET}, {@code POST}) to handler functions
	 * in the {@link ProductHandler}.
	 * </p>
	 * 
	 * @param handler the handler component containing the logic for processing
	 *                requests.
	 * @return a {@link RouterFunction} that contains all the route mappings.
	 */
	@Bean
	RouterFunction<ServerResponse> routes(ProductHandler handler) {

		// Example of a simple route lambda: route(GET("/api/v2/products"), request ->
		// handler.listProduct(request));

		// Chained routes definition
		return route(GET("/api/v2/products"), handler::listProduct) 
				.andRoute(GET("/api/v2/products/{id}"), handler::seeProduct) 
				.andRoute(POST("/api/v2/products"), handler::createProduct) 
				.andRoute(PUT("/api/v2/products/{id}"), handler::updateProduct) 
				.andRoute(DELETE("/api/v2/products/{id}"), handler::deleteProduct) 
				.andRoute(POST("/api/v2/products/upload/{id}"), handler::uploadImage) 
				.andRoute(POST("/api/v2/products/createWithPic"), handler::createProductWithImage); 
	}
}
