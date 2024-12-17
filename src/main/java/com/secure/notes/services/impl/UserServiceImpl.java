package com.secure.notes.services.impl;

import com.secure.notes.Utils.NotesUtil;
import com.secure.notes.constants.CommonConstants;
import com.secure.notes.dtos.UserDTO;
import com.secure.notes.entities.AppRole;
import com.secure.notes.entities.Role;
import com.secure.notes.entities.User;
import com.secure.notes.exception.UserNotFoundException;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.models.StatusResponse;
import com.secure.notes.repository.RoleRepository;
import com.secure.notes.repository.UserRepository;
import com.secure.notes.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.bridge.ICommand;
import org.hibernate.tool.schema.internal.StandardAuxiliaryDatabaseObjectExporter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.SimpleAttributeSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public ResponseEntity<ResponseObject> getAllUsers() {

        StatusResponse statusResponse;
        List<String> message;
        List<User> userList;

        userList = userRepository.findAll();
        if (userList.isEmpty()) {
            log.info("Data is not present in the database");
            message = Collections.singletonList("Data is not available in the database");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        } else {
            log.info("Data is present in the database");
            message = Collections.singletonList("Data is present in the  database");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_STATUS, null);
        }
        return NotesUtil.buildResponseEntityObject(statusResponse, userList);
    }

    /*
        @PreAuthorize("#userId == principal.id")
    */
    @Override
    public ResponseEntity<ResponseObject> updateUserRole(Long userId, String roleName) {

        List<String> message;
        StatusResponse statusResponse;

        Optional<User> user = userRepository.findById(userId);
        AppRole appRole = AppRole.valueOf(roleName);
        Optional<Role> role = roleRepository.findByRoleName(appRole);

        if (user.isPresent() && role.isPresent()) {
            if (!user.get().getRole().equals(role.get())) {
                user.get().setRole(role.get());

            }
            message = Collections.singletonList("Role update Successfully");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);

        } else {
            message = Collections.singletonList("User or Role is not present");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        }
        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }


    @PreAuthorize("#id == principal.id")
    @Override
    public ResponseEntity<ResponseObject> getUserById(Long id) {

        List<String> message;
        StatusResponse statusResponse;
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            message = Collections.singletonList("User Find Successfully");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);
        } else {
            message = Collections.singletonList("User Not Found in the database");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        }
        return NotesUtil.buildResponseEntityObject(statusResponse, user.get());
    }

    @Override
    public ResponseEntity<ResponseObject> deleteUserById(Long userId) {

        List<String> message;
        StatusResponse statusResponse;

        Optional<User> userDB = userRepository.findById(userId);
        if (userDB.isPresent()) {
            log.info("Deletion Successfully for id : {}", userId);
            userRepository.deleteById(userId);
            message = Collections.singletonList("Deletion Successfully for id :" + userId);
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);
        } else {
            message = Collections.singletonList("Invalid User id");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        }

        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

    @Override
    public ResponseEntity<ResponseObject> createUser(UserDTO userDTO) {
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
    public ResponseEntity<ResponseObject> getUserByIdForAdmin(Long id) {

        List<String> message;
        StatusResponse statusResponse;
        Optional<User> dbUser = userRepository.findById(id);
        if (dbUser.isPresent()) {
            message = Collections.singletonList("Data fetched success");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);
            return NotesUtil.buildResponseEntityObject(statusResponse, dbUser.get());
        }
        message = Collections.singletonList("Data is not present related to given id");
        statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.NO_DATA, null);
        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

    @Override
    public ResponseEntity<ResponseObject> changePassword(User user) {
        List<String> message;
        StatusResponse statusResponse;


        User dbUser = userRepository.findByUserName(user.getUserName()).orElseThrow(
                () -> new UserNotFoundException("User Not Found.")
        );

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(dbUser);
            message = Collections.singletonList("Password Changed");
            statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.RESPONSE_SUCCESS, null);
            return NotesUtil.buildResponseEntityObject(statusResponse, null);
        }
        message = Collections.singletonList("Your Password is same as previous");
        statusResponse = NotesUtil.buildStatusResponse(message, CommonConstants.RESPONSE_SUCCESS_CODE, CommonConstants.INVALID_REQUEST_BODY, null);

        return NotesUtil.buildResponseEntityObject(statusResponse, null);
    }

}
