package com.secure.notes.services.impl;

import com.secure.notes.Utils.NotesUtil;
import com.secure.notes.constants.CommonConstants;
import com.secure.notes.dtos.UserDTO;
import com.secure.notes.entities.AppRole;
import com.secure.notes.entities.Role;
import com.secure.notes.entities.User;
import com.secure.notes.jwt.JwtUtils;
import com.secure.notes.models.LoginRequest;
import com.secure.notes.models.LoginResponse;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import com.secure.notes.repository.RoleRepository;
import com.secure.notes.repository.UserRepository;
import com.secure.notes.services.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final BlacklistService blacklistService;

    public UserAuthenticationServiceImpl(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, BlacklistService blacklistService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.blacklistService = blacklistService;
    }

    @Override
    public ResponseEntity<ResponseObject> saveUser(UserDTO userDTO) {

        List<String> message;
        StatusResponse statusResponse;
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);


        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
        user.setRole(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setSignUpMethod("email");
        user.setTwoFactorEnabled(false);
        User savedUser = userRepository.save(user);
        message = Collections.singletonList("Save Successfully");
        statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);

        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

    @Override
    public ResponseEntity<ResponseObject> signInUser(LoginRequest loginRequest) {
        List<String> message;
        StatusResponse statusResponse;
        LoginResponse loginResponse = null;
        Authentication authentication = null;
        try {
            log.info("Authenticate user");
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtils.generateTokenFromUsername(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            loginResponse = new LoginResponse(token, userDetails.getUsername(), roles);

            message = Collections.singletonList("Login Successfully");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);

        } catch (AuthenticationException exception) {

            message = Collections.singletonList("Bad Credentials");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.INVALID_CREDENTIAL, null);
        }

        return NotesUtil.buildResponseEntityObject(statusResponse, loginResponse);
    }

    @Override
    public ResponseEntity<ResponseObject> signOutUser(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer "
        return blacklistService.blacklistToken(token);
    }
}
