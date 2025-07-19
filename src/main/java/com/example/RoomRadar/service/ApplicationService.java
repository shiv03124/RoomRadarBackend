package com.example.RoomRadar.service;

import com.example.RoomRadar.DTO.ApplicationDTO;
import com.example.RoomRadar.Enum.ApplicationStatus;

import java.util.List;

public interface ApplicationService {
    ApplicationDTO createApplication(ApplicationDTO dto);
    ApplicationDTO getApplicationById(Long id);
    List<ApplicationDTO> getAllApplications();
    ApplicationDTO updateApplication(Long id, ApplicationDTO dto);
    void deleteApplication(Long id);
    List<ApplicationDTO> getApplicationsByUserId(Long id);
    List<ApplicationDTO> getApplicationsByRoomId(Long id);
    void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus);
    ApplicationStatus getApplicationStatus(Long userId, Long roomId);

}
