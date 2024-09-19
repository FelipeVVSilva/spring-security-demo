package com.felipeveiga.catalog.entities.dto;

import com.felipeveiga.catalog.entities.User;
import com.felipeveiga.catalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
	private static final long serialVersionUID = 1L;

	@Size(min = 8, max = 64, message = "Password must have minimun 8 characteres and maximun 64 characteres")
	private String password;

	public UserInsertDTO() {
		super();
	}
	public UserInsertDTO(Long id, String firstName, String lastName, String email, String password) {
		super(id, firstName, lastName, email);
		this.password = password;
	}
	public UserInsertDTO(User entity, String password) {
		super(entity);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
