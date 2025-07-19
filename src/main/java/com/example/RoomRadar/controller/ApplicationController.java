package com.example.RoomRadar.controller;

import com.example.RoomRadar.DTO.ApplicationDTO;
import com.example.RoomRadar.Enum.ApplicationStatus;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.Repository.ApplicationRepository;
import com.example.RoomRadar.Repository.RoomRepository;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping
    public ApplicationDTO createApplication(@RequestBody ApplicationDTO dto) {
        return applicationService.createApplication(dto);
    }

    @GetMapping("/{id}")
    public ApplicationDTO getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id);
    }

    @GetMapping
    public List<ApplicationDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PutMapping("/{id}")
    public ApplicationDTO updateApplication(@PathVariable Long id, @RequestBody ApplicationDTO dto) {
        return applicationService.updateApplication(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
    }

    @GetMapping("/user/{userId}")
    public List<ApplicationDTO> getApplicationsByUserId(@PathVariable Long userId) {
        return applicationService.getApplicationsByUserId(userId);
    }

    @GetMapping("/room/{id}")
    public List<ApplicationDTO> getApplicationsByRoomId(@PathVariable Long id) {
        return applicationService.getApplicationsByRoomId(id);
    }

    @PutMapping("/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id, @RequestParam("status") ApplicationStatus status) {
        applicationService.updateApplicationStatus(id, status);
        return "Application status updated to: " + status;
    }

    @GetMapping("/check")
    public ResponseEntity<?> hasUserApplied(@RequestParam Long userId, @RequestParam Long roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean applied = applicationRepository.existsByApplicantAndRoom(user, room);
        return ResponseEntity.ok(Map.of("applied", applied));
    }

    @GetMapping("/status")
    public ResponseEntity<ApplicationStatus> getApplicationStatus(
                @RequestParam Long userId,
            @RequestParam Long roomId) {
        ApplicationStatus status = applicationService.getApplicationStatus(userId, roomId);
        return ResponseEntity.ok(status);
    }




}
