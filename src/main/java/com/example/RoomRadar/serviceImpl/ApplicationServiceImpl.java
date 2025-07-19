package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.DTO.ApplicationDTO;
import com.example.RoomRadar.Enum.ApplicationStatus;
import com.example.RoomRadar.Model.Application;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.Repository.ApplicationRepository;
import com.example.RoomRadar.Repository.RoomRepository;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.mapper.ApplicationMapper;
import com.example.RoomRadar.service.ApplicationService;
import com.example.RoomRadar.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public ApplicationDTO createApplication(ApplicationDTO dto) {
        User user = userRepository.findById(dto.getApplicantId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Prevent duplicate application
        if (applicationRepository.existsByApplicantAndRoom(user, room)) {
            throw new IllegalStateException("User has already applied to this room");
        }

        Application application = ApplicationMapper.toEntity(dto, user, room);
        application.setAppliedAt(LocalDateTime.now());

        Application savedApplication = applicationRepository.save(application);

        // ✅ Send email to room owner
        User owner = room.getUser();
        if (owner != null && owner.getEmail() != null) {
            String subject = "New Application for Your Room Listing";
            String message = String.format(
                    "Hello %s,\n\n" +
                            "Your room listing titled \"%s\" has received a new application from %s.\n" +
                            "Message from applicant: %s\n\n" +
                            "Please log in to your RoomRadar dashboard to review this application.\n\n" +
                            "Regards,\nRoomRadar Team",
                    owner.getFullName(),
                    room.getTitle(),
                    user.getFullName(),
                    dto.getMessage() != null ? dto.getMessage() : "No message provided"
            );

            emailService.sendEmail(owner.getEmail(), subject, message);
        }

        return ApplicationMapper.toDTO(savedApplication);
    }


    @Override
    public ApplicationDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return ApplicationMapper.toDTO(application);
    }

    @Override
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(ApplicationMapper::toDTO)
                .toList();
    }

    @Override
    public ApplicationDTO updateApplication(Long id, ApplicationDTO dto) {
        Application existing = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        existing.setStatus(dto.getStatus());

        return ApplicationMapper.toDTO(applicationRepository.save(existing));
    }

    @Override
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        applicationRepository.delete(application);
    }

    @Override
    public List<ApplicationDTO> getApplicationsByUserId(Long userId) {
        List<Application> applications = applicationRepository.findByApplicantId(userId);
        return applications.stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDTO> getApplicationsByRoomId(Long id) {
        List<Application> applications = applicationRepository.findByRoomId(id);
        return applications.stream()
                .map(ApplicationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        application.setStatus(newStatus);
        applicationRepository.save(application);

        // ✅ Send email to the applicant
        User applicant = application.getApplicant();
        Room room = application.getRoom();

        if (applicant != null && applicant.getEmail() != null) {
            String subject = "Your Room Application Status Has Been Updated";
            String message = String.format(
                    "Hello %s,\n\n" +
                            "The status of your application for the room titled \"%s\" has been updated to: %s.\n" +
                            "You can log in to your RoomRadar account to view the details.\n\n" +
                            "Thank you for using RoomRadar.\n\n" +
                            "Regards,\nRoomRadar Team",
                    applicant.getFullName(),
                    room.getTitle(),
                    newStatus.name()
            );

            emailService.sendEmail(applicant.getEmail(), subject, message);
        }
    }


    @Override
    public ApplicationStatus getApplicationStatus(Long userId, Long roomId) {
        return applicationRepository.findStatusByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }


}
