package com.example.security.user;

import com.example.security.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        user.setBirthdate(userDtoRequest.getBirthdate());
        userRepository.save(user);
    }

    public List<UserDtoResponse> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapUserToUserDtoResponse).collect(Collectors.toList());
    }

    private UserDtoResponse mapUserToUserDtoResponse(User user) {
        UserDtoResponse userDto = new UserDtoResponse();
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setImageUrl(user.getImageUrl());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setBio(user.getBio());
        userDto.setBirthdate(user.getBirthdate());
        return userDto;
    }

    public UserDtoResponse findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(this::mapUserToUserDtoResponse).orElse(null);
    }

}
