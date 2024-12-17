package com.secure.notes.controller;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.models.LoginRequest;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.services.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final UserAuthenticationService userAuthenticationService;

    public PublicController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }


    @PostMapping("/create-user")
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserDTO userDTO) {

        log.info("URL : {} , MethodType : {} , RequestBody : {} ", "api/public/create-user", "POST", userDTO);
        ResponseEntity<ResponseObject> response = userAuthenticationService.saveUser(userDTO);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , ResponseBody : {} ", "/api/public/create-user", "POST", userDTO, response);
        return response;

    }

    @PostMapping("/signin/user")
    public ResponseEntity<ResponseObject> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("URL : {} , MethodType : {} , RequestBody : {} ", "api/public/signin/user", "POST", loginRequest);
        ResponseEntity<ResponseObject> response = userAuthenticationService.signInUser(loginRequest);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , ResponseBody : {} ", "/api/public/signin/user", "POST", loginRequest, response);
        return response;

    }

    @PostMapping("/signout/user")
    public ResponseEntity<ResponseObject> signOut(HttpServletRequest request) {

        log.info("URL : {} , MethodType : {} , RequestBody : {} ", "api/public/signout/user", "POST", request);
        ResponseEntity<ResponseObject> response = userAuthenticationService.signOutUser(request);
        log.info("URL : {} , MethodType : {} , RequestBody : {} , ResponseBody : {} ", "/api/public/signout/user", "POST", request, response);
        return response;

    }

}
