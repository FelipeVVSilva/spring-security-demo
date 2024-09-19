package com.felipeveiga.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.felipeveiga.catalog.entities.Product;
import com.felipeveiga.catalog.tests.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	private long existingId;
	long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	
	
	@Test
	public void deleteShouldDeleteProductWhenIdExists() {

		repo.deleteById(existingId);

		Optional<Product> result = repo.findById(existingId);

		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		product = repo.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	@Test
	public void findByIdShouldReturnsProductWhenIdExists() {
		
		Optional<Product> product = repo.findById(existingId);
		
		Assertions.assertNotNull(product.get().getId());
		Assertions.assertTrue(product.isPresent());
		
		
	}
	
	@Test
	public void findByIdShouldReturnsNothingtWhenIdNotExists() {
		
		Optional<Product> product = repo.findById(nonExistingId);
		
		Assertions.assertFalse(product.isPresent());
		
	}
	
	
}
