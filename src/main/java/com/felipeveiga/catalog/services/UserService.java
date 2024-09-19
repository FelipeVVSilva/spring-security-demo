package com.felipeveiga.catalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.catalog.entities.Role;
import com.felipeveiga.catalog.entities.User;
import com.felipeveiga.catalog.entities.dto.RoleDTO;
import com.felipeveiga.catalog.entities.dto.UserDTO;
import com.felipeveiga.catalog.entities.dto.UserInsertDTO;
import com.felipeveiga.catalog.entities.dto.UserUpdateDTO;
import com.felipeveiga.catalog.repositories.UserRepository;
import com.felipeveiga.catalog.services.exceptions.DatabaseException;
import com.felipeveiga.catalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable){
		Page<User> users = repo.findAll(pageable);
		Page<UserDTO> usersDTO = users.map(user -> new UserDTO(user));
		return usersDTO;
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id of user not found"));
		return new UserDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public UserDTO findUserByEmail(String email) {
		
		User user = repo.findUserByEmail(email);
		
		if (user != null) {
			return new UserDTO(user);
		}
		return null;
		
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		dto.setId(null);
		User entity = new User(dto);
	 	entity.setPassword(encoder.encode(dto.getPassword()));
		for (RoleDTO roleDTO : dto.getRoles()) {
			entity.addRole(new Role(roleDTO));
		}
		
		entity = repo.save(entity);
		
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO updatedUser) {
		try {
			User user = repo.getReferenceById(id);
			updateUserData(updatedUser, user);
			user = repo.save(user);
			return new UserDTO(user);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id of user not found");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		try {
			if(!repo.existsById(id)) {
				throw new ResourceNotFoundException("Id of user not found");
			}
			else {
				repo.deleteById(id);
			}
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Do not delete this category, as it has associations. This action may cause integrity violations in the database.");
		}
	}
	
	private void updateUserData(UserDTO updatedUser, User user) {
		user.setFirstName(updatedUser.getFirstName());
		user.setLastName(updatedUser.getLastName());
		user.setEmail(updatedUser.getEmail());
		
		user.getRoles().clear();
		
		updatedUser.getRoles().forEach(role -> user.getRoles().add(new Role(role)));
	}
	
}
