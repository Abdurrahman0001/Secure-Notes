package com.secure.notes.services;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void updateUserRole(Long userId,String roleName);
    UserDTO getUserById(Long id);

    void deleteUserById(Long userId);

    UserDTO createUser(User user);

    UserDTO getUserByIdForAdmin(Long id);

    ResponseEntity<String> changePassword(User user);
}
