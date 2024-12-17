package com.secure.notes.controller;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.entities.User;
import com.secure.notes.models.ResponseObject;
import com.secure.notes.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<ResponseObject> getUser(@PathVariable Long id){

        log.info("URL : {} , MethodType : {} , PathVariable : {} ", "api/users/id/get", "GET", id);
        ResponseEntity<ResponseObject> response = userService.getUserById(id);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , ResponseBody : {} ", "/api/users/id/get", "GET", id, response);
        return response;
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody User user){

        log.info("URL : {} , MethodType : {} , PathVariable : {} ", "api/users/change-password", "POST", user);
        ResponseEntity<ResponseObject> response = userService.changePassword(user);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , ResponseBody : {} ", "/api/users/change-password", "POST", user, response);
        return response;

    }



}
