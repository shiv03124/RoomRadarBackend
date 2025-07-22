package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.Model.Admin;
import com.example.RoomRadar.Repository.AdminRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.mapper.AdminMapper;
import com.example.RoomRadar.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public AdminDTO createAdmin(AdminDTO dto) {
        Admin admin = AdminMapper.toEntity(dto);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        Admin saved = adminRepository.save(admin);
        return AdminMapper.toDTO(saved);
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        return AdminMapper.toDTO(admin);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(AdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO dto) {
        Admin existing = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());
        existing.setUpdatedAt(LocalDateTime.now());

        Admin updated = adminRepository.save(existing);
        return AdminMapper.toDTO(updated);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(id);
    }

}
