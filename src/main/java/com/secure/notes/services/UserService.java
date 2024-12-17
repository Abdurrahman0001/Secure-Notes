package com.secure.notes.services;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.entities.User;
import com.secure.notes.models.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<ResponseObject> getAllUsers();

    ResponseEntity<ResponseObject> updateUserRole(Long userId, String roleName);

    ResponseEntity<ResponseObject> getUserById(Long id);

    ResponseEntity<ResponseObject> deleteUserById(Long userId);

    ResponseEntity<ResponseObject> createUser(UserDTO userDTO);

    ResponseEntity<ResponseObject> getUserByIdForAdmin(Long id);

    ResponseEntity<ResponseObject> changePassword(User user);
}
