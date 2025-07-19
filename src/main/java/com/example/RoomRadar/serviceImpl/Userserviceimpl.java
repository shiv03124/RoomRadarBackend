package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.DTO.UserDTO;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.Repository.EmailOtpRepository;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.mapper.UserMapper;
import com.example.RoomRadar.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class Userserviceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailOtpRepository otpRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO, MultipartFile imageFile) throws IOException {


        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Check for existing phone
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new IllegalArgumentException("Phone number already in use");
        }


        User user = UserMapper.toEntity(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            user.setProfileImageUrl(imageUrl);
        }

        if (user.getRooms() != null) {
            for (Room room : user.getRooms()) {
                room.setUser(user);
            }
        }

        User savedUser = userRepository.save(user); // Cascade will save rooms too
        otpRepository.deleteByEmail(userDTO.getEmail());
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public String checkEmailExists(String email) {
        return userRepository.existsByEmail(email) ? "exists" : "not";
    }

    @Override
    public String checkPhoneExists(String phone) {
        return userRepository.existsByPhone(phone) ? "exists" : "not";
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO, MultipartFile imageFile) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAddress(userDTO.getAddress());
        user.setCurrentCity(userDTO.getCurrentCity());
        user.setUpdatedAt(LocalDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile);
            user.setProfileImageUrl(imageUrl);
        }

        return UserMapper.toDTO(userRepository.save(user));
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }




    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }


//    @Override
//    public GoogleIdToken.Payload verifyGoogleToken(String token) throws Exception {
//        try {
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
//                    .Builder(new NetHttpTransport(), new JacksonFactory())
//                    .setAudience(Collections.singletonList("299037829972-djf5trl1ei4fb9fqo88rc7ipsf8kskr9.apps.googleusercontent.com")) // Replace with your client ID
//                    .build();
//
//            GoogleIdToken idToken = verifier.verify(token);
//            return idToken != null ? idToken.getPayload() : null;
//
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @Override
//    public User findOrCreateGoogleUser(String email) {
//        if (email == null) {
//            throw new IllegalArgumentException("Email cannot be null");
//        }
//
//        return userRepository.findByEmail(email).orElseGet(() -> {
//            User newUser = new User();
//            newUser.setEmail(email);
//            newUser.setFullName(" ");
//            newUser.setPassword(UUID.randomUUID().toString());
//            newUser.setCreatedAt(LocalDateTime.now());
//            newUser.setUpdatedAt(LocalDateTime.now());
//            return userRepository.save(newUser);
//        });
//    }





}
