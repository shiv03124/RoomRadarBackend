package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.Model.*;
import com.example.RoomRadar.Repository.AdminRepository;
import com.example.RoomRadar.Repository.ApplicationRepository;
import com.example.RoomRadar.Repository.RoomRepository;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.exception.ResourceNotFoundException;
import com.example.RoomRadar.mapper.RoomMapper;
import com.example.RoomRadar.service.EmailService;
import com.example.RoomRadar.service.RoomService;
import com.example.RoomRadar.utils.RoomSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.RoomRadar.Model.RoomStatus.APPROVED;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public RoomDTO createRoomWithImages(RoomDTO roomDTO, MultipartFile[] imageFiles, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Room room = RoomMapper.toEntity(roomDTO, user,null);
        room.setUser(user);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());
        room.setStatus(APPROVED); // Set default status if needed

        List<String> uploadedImageUrls = new ArrayList<>();
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadFile(file);
                    uploadedImageUrls.add(imageUrl);
                }
            }
        }

        room.setStatus(RoomStatus.PENDING);
        room.setImages(uploadedImageUrls);

        Room savedRoom = roomRepository.save(room);

        return RoomMapper.toDTO(savedRoom);
    }


    @Override
    @Transactional
    public RoomDTO updateRoomWithImages(Long roomId, RoomDTO roomDTO, MultipartFile[] imageFiles) throws IOException {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        // Update fields
        existingRoom.setTitle(roomDTO.getTitle());
        existingRoom.setDescription(roomDTO.getDescription());
        existingRoom.setAddress(roomDTO.getAddress());
        existingRoom.setCity(roomDTO.getCity());
        existingRoom.setArea(roomDTO.getArea());
        existingRoom.setAccommodationType(roomDTO.getAccommodationType());
        existingRoom.setFurnished(roomDTO.getFurnished());
        existingRoom.setAvailableFrom(roomDTO.getAvailableFrom());
        existingRoom.setRent(roomDTO.getRent());
        existingRoom.setSecurityDeposit(roomDTO.getSecurityDeposit());
        existingRoom.setPreferredGender(roomDTO.getPreferredGender());
        existingRoom.setIsAvailable(roomDTO.getIsAvailable());
        existingRoom.setAmenities(roomDTO.getAmenities());
        existingRoom.setUpdatedAt(LocalDateTime.now());
        existingRoom.setConfiguration(roomDTO.getConfiguration());

        // Upload new images if provided
        if (imageFiles != null && imageFiles.length > 0) {
            List<String> uploadedImageUrls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadFile(file);
                    uploadedImageUrls.add(imageUrl);
                }
            }
            existingRoom.setImages(uploadedImageUrls);
        }

        Room updatedRoom = roomRepository.save(existingRoom);
        return RoomMapper.toDTO(updatedRoom);
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found or not approved"));

        return RoomMapper.toDTO(room);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findByStatus(RoomStatus.valueOf("APPROVED"));
        return rooms.stream()
                .map(RoomMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAllRoomsForAdmin() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(RoomMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        List<Application> applications = room.getApplications();

        // Send email to all applicants
        for (Application app : applications) {
            User applicant = app.getApplicant();
            String email = applicant.getEmail();
            String subject = "Room Removed from RoomRadar";
            String message = "Hi " + applicant.getFullName() + ",\n\n" +
                    "We're sorry to inform you that the room you applied for (\"" + room.getTitle() + "\") has been removed by the owner.\n" +
                    "Please explore other listings on RoomRadar.\n\n" +
                    "Thanks,\nRoomRadar Team";

            emailService.sendEmail(email, subject, message);  // ⬅️ you'll implement this
        }


        // Delete room
        roomRepository.delete(room);
    }


    @Override
    public List<RoomDTO> getApprovedRoomsByAccommodationType(String accommodationType) {
        List<Room> rooms = roomRepository.findByStatusAndAccommodationType(RoomStatus.APPROVED, accommodationType);
        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getApprovedRoomsNotAppliedByUser(String accommodationType, Long userId) {
        List<Room> rooms = roomRepository.findApprovedAndNotAppliedByUser(RoomStatus.APPROVED, accommodationType, userId);
        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<RoomDTO> getRoomsByUserId(Long userId) {
        List<Room> rooms = roomRepository.findByStatusAndUserId(APPROVED, userId);
        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<RoomDTO> getRoomsExcludingUser(Long userId) {
        List<Room> rooms = roomRepository.findByStatusAndUserIdNot(APPROVED, userId);
        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<RoomDTO> getAvailableRooms(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Long> appliedRoomIds = applicationRepository.findByApplicantId(userId).stream()
                .map(app -> app.getRoom().getId()).collect(Collectors.toList());

        List<Room> rooms = appliedRoomIds.isEmpty()
                ? roomRepository.findByStatusAndUserIdNot(APPROVED, userId)
                : roomRepository.findByStatusAndUserIdNotAndIdNotIn(APPROVED, userId, appliedRoomIds);

        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }



    @Override
    public List<RoomDTO> getAppliedRooms(Long userId) {
        List<Application> applications = applicationRepository.findByApplicantId(userId);
        List<Room> rooms = applications.stream()
                .map(Application::getRoom)
                .filter(room -> room.getStatus() == APPROVED)
                .collect(Collectors.toList());

        return rooms.stream().map(RoomMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<RoomDTO> getRoomsNotAppliedByUser(Long userId) {
        // Already returns approved and not applied rooms
        List<Room> rooms = roomRepository.findApprovedRoomsNotAppliedByUser(userId);

        // Now exclude rooms created by the same user
        List<Room> filteredRooms = rooms.stream()
                .filter(room -> room.getUser() != null && !room.getUser().getId().equals(userId))
                .toList();

        return filteredRooms.stream()
                .map(RoomMapper::toDTO)
                .collect(Collectors.toList());
    }





    @Override
    public List<RoomDTO> searchRooms(String city, String area, Integer minVacancies, Integer maxVacancies,
                                     Double minRent, Double maxRent, String preferredGender,
                                     List<String> amenities, Boolean isAvailable, Long userId) {

        Specification<Room> spec = Specification.where(RoomSpecifications.hasStatus(RoomStatus.APPROVED))
                .and(RoomSpecifications.hasCity(city))
                .and(RoomSpecifications.hasArea(area))
                .and(RoomSpecifications.vacanciesBetween(minVacancies, maxVacancies))
                .and(RoomSpecifications.rentBetween(minRent, maxRent))
                .and(RoomSpecifications.hasGender(preferredGender));

        // Add amenities if provided
        if (amenities != null) {
            for (String amenity : amenities) {
                spec = spec.and(RoomSpecifications.hasAmenity(amenity));
            }
        }

        // Filter by availability if provided
        if (isAvailable != null) {
            spec = spec.and(RoomSpecifications.isAvailable(isAvailable));
        }

        // Filter out rooms already applied by user
        if (userId != null) {
            List<Long> appliedRoomIds = applicationRepository.findAppliedRoomIdsByUserId(userId);
            if (!appliedRoomIds.isEmpty()) {
                spec = spec.and((root, query, cb) -> cb.not(root.get("id").in(appliedRoomIds)));
            }

            // ✅ Exclude rooms added by the user themselves
            spec = spec.and((root, query, cb) -> cb.notEqual(root.get("user").get("id"), userId));
        }

        return roomRepository.findAll(spec).stream()
                .map(RoomMapper::toDTO)
                .collect(Collectors.toList());
    }




    @Override
    public List<RoomDTO> getRoomsByStatus(RoomStatus status) {
        List<Room> rooms = roomRepository.findByStatus(status);
        return rooms.stream()
                .map(RoomMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomDTO updateRoomStatus(Long roomId, String status, Long adminId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

        // Update status & approvedBy
        RoomStatus newStatus = RoomStatus.valueOf(status);
        room.setStatus(newStatus);
        room.setApprovedBy(admin);
        room.setUpdatedAt(LocalDateTime.now());

        Room updatedRoom = roomRepository.save(room);

        // Send email notification to room owner
        User owner = room.getUser();
        if (owner != null && owner.getEmail() != null) {
            String email = owner.getEmail();
            String subject = "Your Room Listing Status Updated";
            String message = String.format(
                    "Hello %s,\n\n" +
                            "Your room listing titled \"%s\" has been updated to status: %s.\n" +
                            "If you have any questions, please contact support.\n\n" +
                            "Regards,\nRoomRadar Team",
                    owner.getFullName(),
                    room.getTitle(),
                    newStatus.name()
            );
            emailService.sendEmail(email, subject, message);
        }

        return RoomMapper.toDTO(updatedRoom);
    }


}
