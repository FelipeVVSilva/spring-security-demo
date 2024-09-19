package com.felipeveiga.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipeveiga.catalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
