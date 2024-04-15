package com.example.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/edit")
    public ResponseEntity editUser(@RequestBody UserDtoRequest userDtoRequest) {
        userService.editUser(userDtoRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

}
