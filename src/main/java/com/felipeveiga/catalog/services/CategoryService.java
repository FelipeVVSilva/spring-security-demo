package com.felipeveiga.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.catalog.entities.Category;
import com.felipeveiga.catalog.entities.dto.CategoryDTO;
import com.felipeveiga.catalog.repositories.CategoryRepository;
import com.felipeveiga.catalog.services.exceptions.DatabaseException;
import com.felipeveiga.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable){
		Page<Category> categories = repo.findAll(pageable);
		Page<CategoryDTO> categoriesDTO = categories.map(cat -> new CategoryDTO(cat));
		return categoriesDTO;
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id of category not found"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		dto.setId(null);
		Category entity = new Category(dto);
		entity = repo.save(entity);
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO updatedCategory) {
		try {
			Category cat = repo.getReferenceById(id);
			updatedCategoryData(updatedCategory, cat);
			cat = repo.save(cat);
			return new CategoryDTO(cat);
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
	
	private void updatedCategoryData(CategoryDTO updatedCategory, Category cat) {
		cat.setName(updatedCategory.getName());
	}
	
	
	
	
}
