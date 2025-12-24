package com.egui.gabo.webflux.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.egui.gabo.webflux.api.handler.ProductHandler;

@Configuration
public class RouterFunctionConfig {


    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler) {

		//route(GET("/api/v2/products"), request -> handler.listProduct(request));
    	
		return route(GET("/api/v2/products"), handler::listProduct)
				.andRoute(GET("/api/v2/products/{id}"), handler::seeProduct)
				.andRoute(POST("/api/v2/products"), handler::createProduct)
				.andRoute(PUT("/api/v2/products/{id}"), handler::updateProduct)
				.andRoute(DELETE("/api/v2/products/{id}"), handler::deleteProduct)
				.andRoute(POST("/api/v2/products/upload/{id}"), handler::uploadImage)
				.andRoute(POST("/api/v2/products/createWithPic"), handler::createProductWithImage);
		
	}
}
