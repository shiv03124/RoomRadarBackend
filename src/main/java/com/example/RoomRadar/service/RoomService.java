package com.example.RoomRadar.service;

import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.Model.RoomStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    RoomDTO createRoomWithImages(RoomDTO roomDTO, MultipartFile[] images, Long userId) throws IOException;

    RoomDTO updateRoomWithImages(Long roomId, RoomDTO roomDTO, MultipartFile[] imageFiles) throws IOException;


    List<RoomDTO> getRoomsByStatus(RoomStatus status);
    List<RoomDTO> getRoomsNotAppliedByUser(Long userId);

    // Returns approved room by ID
    RoomDTO getRoomById(Long roomId);
    List<RoomDTO> getApprovedRoomsByAccommodationType(String accommodationType);
    List<RoomDTO> getApprovedRoomsNotAppliedByUser(String accommodationType, Long userId);
    List<RoomDTO> getAllRooms();
    List<RoomDTO> getAllRoomsForAdmin();

    void deleteRoom(Long roomId);

    // Returns approved rooms posted by a user
    List<RoomDTO> getRoomsByUserId(Long id);

    // Returns approved rooms excluding those posted by a user
    List<RoomDTO> getRoomsExcludingUser(Long id);

    // Returns approved rooms user has applied to
    List<RoomDTO> getAppliedRooms(Long userId);

    // Returns approved rooms available for the user (not owned or applied)
    List<RoomDTO> getAvailableRooms(Long userId);

    RoomDTO updateRoomStatus(Long roomId, String status, Long adminId);


    // Search approved rooms with multiple filters including availability
    List<RoomDTO> searchRooms(String city, String area, Integer minVacancies, Integer maxVacancies,
                              Double minRent, Double maxRent, String preferredGender, List<String> amenities, Boolean isAvailable, Long userId);
}
