package com.example.RoomRadar.auth;

import com.example.RoomRadar.DTO.JwtResponse;
import com.example.RoomRadar.DTO.UserLoginRequest;
import com.example.RoomRadar.Model.Admin;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.Repository.AdminRepository;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.security.AdminDetailsService;
import com.example.RoomRadar.security.JwtUtils;
import com.example.RoomRadar.service.AdminService;
import com.example.RoomRadar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserLogin {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody UserLoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String token = jwtUtils.generateJwtToken(loginRequest.getEmail());
        return new JwtResponse(token); // 🔥 return as JSON
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> adminLogin(@RequestBody UserLoginRequest loginRequest) {
        String email = loginRequest.getEmail().trim();
        String rawPassword = loginRequest.getPassword();

        System.out.println("Login endpoint hit for: " + email);

        try {
            Admin admin = adminRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

            // ✅ Plain-text password comparison
            if (!admin.getPassword().equals(rawPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }

            // ✅ Generate JWT token after successful manual authentication
            String token = jwtUtils.generateJwtToken(admin.getEmail());

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }






//    @PostMapping("/auth/google")
//    public ResponseEntity<?> googleLogin(@RequestParam String email) {
//        try {
//            User user = userService.findOrCreateGoogleUser(email);
//
//            // Log to verify user is returned or created
//            System.out.println("User ID: " + user.getId());
//
//            String token = jwtUtils.generateJwtToken(user.getEmail());
//
//            return ResponseEntity.ok(Map.of(
//                    "user", user,
//                    "token", token
//            ));
//        } catch (Exception e) {
//            e.printStackTrace(); // log full error
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error processing Google login: " + e.getMessage());
//        }
//    }



}
