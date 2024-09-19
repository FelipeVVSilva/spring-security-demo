package com.felipeveiga.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.catalog.entities.Category;
import com.felipeveiga.catalog.entities.Product;
import com.felipeveiga.catalog.entities.dto.CategoryDTO;
import com.felipeveiga.catalog.entities.dto.ProductDTO;
import com.felipeveiga.catalog.repositories.ProductRepository;
import com.felipeveiga.catalog.services.exceptions.DatabaseException;
import com.felipeveiga.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable){
		Page<Product> products = repo.findAll(pageable);
		Page<ProductDTO> productsDTO = products.map(pro -> new ProductDTO(pro));
		return productsDTO;
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id of category not found"));
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		dto.setId(null);
		Product entity = new Product(dto);
		
		for (CategoryDTO catDTO : dto.getCategories()) {
			entity.addCategory(new Category(catDTO));
		}
		
		entity = repo.save(entity);
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO updatedProduct) {
		try {
			Product product = repo.getReferenceById(id);
			updateProductData(updatedProduct, product);
			product = repo.save(product);
			return new ProductDTO(product, product.getCategories());
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id of category not found");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		try {
			if(!repo.existsById(id)) {
				throw new ResourceNotFoundException("Id of category not found");
			}
			else {
				repo.deleteById(id);
			}
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Do not delete this category, as it has associations. This action may cause integrity violations in the database.");
		}
	}
	
	private void updateProductData(ProductDTO updatedProduct, Product product) {
		product.setName(updatedProduct.getName());
		product.setDescription(updatedProduct.getDescription());
		product.setPrice(updatedProduct.getPrice());
		product.setImgUrl(updatedProduct.getImgUrl());
		
		product.getCategories().clear();
		
		updatedProduct.getCategories().forEach(cat -> product.getCategories().add(new Category(cat)));
	}
	
}
