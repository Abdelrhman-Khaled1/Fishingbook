package com.example.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String username) {
       return userRepository.findByEmail(username);
    }
    public void save(User user) {
       userRepository.save(user);
    }
}
