//package com.example.expensetracker.controller;
//
//import com.licenta.backend.exception.ResourceNotFoundException;
//import com.licenta.backend.model.User;
//import com.licenta.backend.model.authentication.UserPrincipal;
//import com.licenta.backend.model.payload.request.ChangePasswordRequest;
//import com.licenta.backend.model.payload.response.ApiResponse;
//import com.licenta.backend.repository.UserRepository;
//import com.licenta.backend.security.CurrentUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    @Autowired
//    private UserRepository userRepository;
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
//}
