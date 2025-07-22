package com.example.RoomRadar.mapper;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.Model.Admin;

public class AdminMapper {

    public static AdminDTO toDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setFullName(admin.getFullName());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole());
        dto.setPassword(admin.getPassword());
        return dto;
    }

    public static Admin toEntity(AdminDTO dto) {
        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setRole(dto.getRole());
        admin.setPassword(dto.getPassword());
        return admin;
    }
}
