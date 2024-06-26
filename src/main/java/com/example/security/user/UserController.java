package com.example.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/edit")
    public ResponseEntity editUser(@RequestBody @Validated UserDtoRequest userDtoRequest) {
        userService.editUser(userDtoRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDtoResponse> getCurrentUser(){
        return new ResponseEntity<>(userService.getCurrentUser(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
    }

    @DeleteMapping("/delete/account")
    public ResponseEntity deleteMyAccount(){
        userService.deleteMyAccount();
        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping("/admin/delete/account/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUserAccount(@PathVariable Long id){
        userService.deleteUserAccount(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
