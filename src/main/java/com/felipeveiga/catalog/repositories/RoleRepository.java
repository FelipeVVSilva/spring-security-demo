package com.felipeveiga.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipeveiga.catalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
