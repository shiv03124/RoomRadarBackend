package com.example.RoomRadar.service;

import com.example.RoomRadar.DTO.UserDTO;
import com.example.RoomRadar.Model.User;
import com.google.api.client.auth.openidconnect.IdToken;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface UserService {
    UserDTO createUser(UserDTO userDTO, MultipartFile imageFile) throws IOException;
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO, MultipartFile imageFile) throws IOException;
    // IdToken.Payload verifyGoogleToken(String token) throws Exception;
    void deleteUser(Long id);
    String checkEmailExists(String email);
    String checkPhoneExists(String phone);
    UserDTO getUserByEmail(String email);
    // User findOrCreateGoogleUser(String email);

}
