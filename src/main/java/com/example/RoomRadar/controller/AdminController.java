package com.example.RoomRadar.controller;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/createAdmin")
    public AdminDTO createAdmin(@RequestBody AdminDTO dto) {
        return adminService.createAdmin(dto);
    }

    @GetMapping("/{id}")
    public AdminDTO getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }

    @GetMapping
    public List<AdminDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PutMapping("/{id}")
    public AdminDTO updateAdmin(@PathVariable Long id, @RequestBody AdminDTO dto) {
        return adminService.updateAdmin(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }


}