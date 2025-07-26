package com.example.RoomRadar.controller;


import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.DTO.UserDTO;
import com.example.RoomRadar.DTO.UserLoginRequest;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.service.EmailService;
import com.example.RoomRadar.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // Create User
    @PostMapping(value = "/saveUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
            @RequestPart("userDTO") String userDTOStr,
            @RequestPart(required = false) MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(userDTOStr, UserDTO.class);

        // Check if OTP was verified for this email
        boolean isOtpVerified = emailService.isOtpVerified(userDTO.getEmail());
        if (!isOtpVerified) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("OTP verification required before registration.");
        }

        // Proceed with user creation
        UserDTO createdUser = userService.createUser(userDTO, image);

        // Optionally, delete OTP after successful user creation
        emailService.clearOtpForEmail(userDTO.getEmail());

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }




    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        String result = userService.checkEmailExists(email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<String> checkPhone(@RequestParam String phone) {
        String result = userService.checkPhoneExists(phone);
        return ResponseEntity.ok(result);
    }



    // Get All Users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update User
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestPart("userDTO") String userDTOStr,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(userDTOStr, UserDTO.class);

        UserDTO updatedUser = userService.updateUser(id, userDTO, image);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


}
