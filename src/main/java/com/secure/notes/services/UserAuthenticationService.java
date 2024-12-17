package com.secure.notes.services;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.models.LoginRequest;
import com.secure.notes.models.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserAuthenticationService {

    ResponseEntity<ResponseObject> saveUser(UserDTO userDTO);

    ResponseEntity<ResponseObject> signInUser(LoginRequest loginRequest);

    ResponseEntity<ResponseObject> signOutUser(HttpServletRequest request);

}
