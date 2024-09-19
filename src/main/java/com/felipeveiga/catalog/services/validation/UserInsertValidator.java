package com.felipeveiga.catalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.felipeveiga.catalog.entities.dto.UserDTO;
import com.felipeveiga.catalog.entities.dto.UserInsertDTO;
import com.felipeveiga.catalog.resources.exceptions.FieldMessage;
import com.felipeveiga.catalog.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserService userService;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		UserDTO userDTO = userService.findUserByEmail(dto.getEmail());
		
		if(userDTO != null) {
			list.add(new FieldMessage("email", "Existing email"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
