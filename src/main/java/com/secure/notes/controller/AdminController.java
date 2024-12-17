package com.secure.notes.controller;


import com.secure.notes.models.ResponseObject;
import com.secure.notes.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/{id}/get")
    public ResponseEntity<ResponseObject> getUser(@PathVariable Long id) {

        log.info("URL : {} , MethodType : {} , PathVariable : {} ", "/api/admin/user/id/get", "GET", id);
        ResponseEntity<ResponseObject> response = userService.getUserByIdForAdmin(id);
        log.info("URL : {} , MethodType : {} , ResponseBody : {} ", "/api/admin/user/id/get", "GET", response);
        return response;
    }

    /*
        @PreAuthorize("hasRole('ADMIN')")
    */
    @GetMapping("/users/get")
    public ResponseEntity<ResponseObject> getAllUsers() {
        log.info("URL : {} , MethodType : {} ", "/api/admin/users/get", "GET");
        ResponseEntity<ResponseObject> response = userService.getAllUsers();

        log.info("URL : {} , MethodType : {} , ResponseBody : {} ", "/api/admin/users/get", "GET", response);
        return response;
    }


    @PutMapping("/role/update")
    public ResponseEntity<ResponseObject> updateUserRole(@RequestParam Long userId,
                                                         @RequestParam String roleName) {
        log.info("URL : {} , MethodType : {} , RequestParameter1 : {} , RequestParameter2 : {} ", "/api/admin/role/update", "PUT", userId, roleName);
        ResponseEntity<ResponseObject> response = userService.updateUserRole(userId, roleName);
        log.info("URL : {} , MethodType : {} , RequestParameter1 : {} , RequestParameter2 : {} ResponseBody {} ", "/api/admin/role/update", "PUT", userId, roleName, response);
        return response;
    }


    @DeleteMapping("/{userId}/delete")
/*
    @PreAuthorize("hasRole('ADMIN')")
*/
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId) {
        log.info("URL : {} , MethodType , {} , PathVariable : {} ", "/api/admin/userId/delete", "DELETE", userId);
        ResponseEntity<ResponseObject> response = userService.deleteUserById(userId);
        log.info("URL : {} , MethodType : {} , PathVariable : {} , ResponseBody : {} ", "/api/admin/userId/delete", "DELETE", userId, response);
        return response;

    }

}
