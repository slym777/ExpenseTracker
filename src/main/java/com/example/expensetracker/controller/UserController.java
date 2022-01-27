package com.example.expensetracker.controller;

import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/changePassword")
//    public ResponseEntity updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @CurrentUser UserPrincipal currentUser) {
//        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentUser.getPassword())){
//            return new ResponseEntity(new ApiResponse(false, "Incorrect Password"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        User user = userRepository.findUserById(currentUser.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
//        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
//        userRepository.save(user);
//
//        return new ResponseEntity(new ApiResponse(true, "Password updated successful"), HttpStatus.OK);
//    }
}
