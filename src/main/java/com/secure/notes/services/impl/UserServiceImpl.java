package com.secure.notes.services.impl;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.models.AppRole;
import com.secure.notes.models.Role;
import com.secure.notes.models.User;
import com.secure.notes.repository.RoleRepository;
import com.secure.notes.repository.UserRepository;
import com.secure.notes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /*
        @PreAuthorize("#userId == principal.id")
    */
    @Override
    public void updateUserRole(Long userId, String roleName) {
        User dbuser = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User Not Found."));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole).orElseThrow(
                () -> new RuntimeException("Role Not Found."));
        if (!dbuser.getRole().equals(role)) {
            dbuser.setRole(role);
        }
        userRepository.save(dbuser);

    }


    @PreAuthorize("#id == principal.id")
    @Override
    public UserDTO getUserById(Long id) {
        User dbUser = userRepository.findById(id).orElseThrow();
        return convertToDTO(dbUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO createUser(User user) {
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
        user.setRole(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setSignUpMethod("email");
        user.setTwoFactorEnabled(false);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO getUserByIdForAdmin(Long id) {
        Optional<User> dbUser = userRepository.findById(id);
        return convertToDTO(dbUser.get());
    }

    @Override
    public ResponseEntity<String> changePassword(User user) {
        User dbUser = userRepository.findByUserName(user.getUserName()).orElseThrow(
                () -> new RuntimeException("User Not Found.")
        );

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(dbUser);
            return ResponseEntity.ok("Your Password Changed.");
        }

        return ResponseEntity.ok("Your Password is same as Previous.");
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

}
