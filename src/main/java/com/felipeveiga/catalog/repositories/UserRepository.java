package com.felipeveiga.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipeveiga.catalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByEmail(String email);
	
}
