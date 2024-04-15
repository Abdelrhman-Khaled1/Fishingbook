package com.example.security.user;

import com.example.security.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
    public void save(User user) {
        userRepository.save(user);
    }

    public void editUser(UserDtoRequest userDtoRequest) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = findByEmail(loggedInUser.getUsername()).get();
        user.setFirstname(userDtoRequest.getFirstname());
        user.setLastname(userDtoRequest.getLastname());
        user.setImageUrl(userDtoRequest.getImageUrl());
        user.setPhone(userDtoRequest.getPhone());
        user.setAddress(userDtoRequest.getAddress());
        user.setBio(userDtoRequest.getBio());
        userRepository.save(user);
    }
}
