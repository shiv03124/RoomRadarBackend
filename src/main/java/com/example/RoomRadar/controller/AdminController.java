package com.example.RoomRadar.controller;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/by-email")
    public AdminDTO getAdminByEmail(@RequestParam String email) {
        try {
            return adminService.getAdminByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // rethrow for now
        }
    }


}