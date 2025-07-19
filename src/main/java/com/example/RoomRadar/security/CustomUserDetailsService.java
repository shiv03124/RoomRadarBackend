package com.example.RoomRadar.security;

import com.example.RoomRadar.Model.Admin;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.Repository.AdminRepository;
import com.example.RoomRadar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


        @Autowired
        private AdminRepository adminRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            // Try user
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("user"))
                );
            }

            // Try admin
            Optional<Admin> adminOpt = adminRepository.findByEmailIgnoreCase(email);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                return new org.springframework.security.core.userdetails.User(
                        admin.getEmail(),
                        admin.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            }

            throw new UsernameNotFoundException("User not found");
        }
    }


