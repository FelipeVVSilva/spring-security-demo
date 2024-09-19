package com.felipeveiga.catalog.tests;

import com.felipeveiga.catalog.entities.Product;
import com.felipeveiga.catalog.entities.dto.ProductDTO;

public class ProductFactory {

	public static Product createProduct() {
		return new Product(1L, "Phone", "Good Phone", 800.0, "http://url");
	}
	
	public static ProductDTO createProductDTO() {
		return new ProductDTO(createProduct());
	}
	
}
