package com.example.RoomRadar.service;

import com.example.RoomRadar.DTO.AdminDTO;

public interface AdminService {
    AdminDTO getAdminByEmail(String email);

}
