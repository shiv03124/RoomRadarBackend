package com.example.RoomRadar.service;

import com.example.RoomRadar.DTO.AdminDTO;

import java.util.List;

public interface AdminService {
    AdminDTO getAdminByEmail(String email);
    AdminDTO createAdmin(AdminDTO dto);
    AdminDTO getAdminById(Long id);
    List<AdminDTO> getAllAdmins();
    AdminDTO updateAdmin(Long id, AdminDTO dto);
    void deleteAdmin(Long id);
}
