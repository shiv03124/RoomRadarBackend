package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.Model.Admin;
import com.example.RoomRadar.Repository.AdminRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.mapper.AdminMapper;
import com.example.RoomRadar.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminserviceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public AdminDTO getAdminByEmail(String email) {
        System.out.println(email);
        Admin admin = adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + email));
        return AdminMapper.toDTO(admin);
    }

}
