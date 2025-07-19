package com.example.RoomRadar.mapper;

import com.example.RoomRadar.DTO.AdminDTO;
import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.DTO.UserDTO;
import com.example.RoomRadar.Model.Admin;
import com.example.RoomRadar.Model.Application;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;

import java.util.List;

public class RoomMapper {

    public static RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setTitle(room.getTitle());
        dto.setDescription(room.getDescription());
        dto.setAddress(room.getAddress());
        dto.setCity(room.getCity());
        dto.setArea(room.getArea());
        dto.setAccommodationType(room.getAccommodationType());
        dto.setFurnished(room.getFurnished());
        dto.setAvailableFrom(room.getAvailableFrom());
        dto.setRent(room.getRent());
        dto.setSecurityDeposit(room.getSecurityDeposit());
        dto.setStatus(room.getStatus());
        dto.setPreferredGender(room.getPreferredGender());
        dto.setConfiguration(room.getConfiguration());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setImages(room.getImages());
        dto.setAmenities(room.getAmenities());
        dto.setNoofvacancies(room.getNoofvacancies());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());

        // Map User -> UserDTO
        if (room.getUser() != null) {
            User user = room.getUser();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            dto.setUser(userDTO);
        }

        // Map Admin -> AdminDTO (approvedBy)
        if (room.getApprovedBy() != null) {
            Admin admin = room.getApprovedBy();
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setId(admin.getId());
            adminDTO.setFullName(admin.getFullName());
            adminDTO.setEmail(admin.getEmail());
            adminDTO.setRole(admin.getRole());
            dto.setApprovedBy(adminDTO);
        }

        // Map Applications -> ApplicationDTO list
        if (room.getApplications() != null) {
            dto.setApplications(room.getApplications().stream()
                    .map(ApplicationMapper::toDTO)
                    .toList());
        }

        return dto;
    }

    public static Room toEntity(RoomDTO dto, User user, Admin admin) {
        Room room = new Room();
        room.setId(dto.getId());
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setAddress(dto.getAddress());
        room.setCity(dto.getCity());
        room.setArea(dto.getArea());
        room.setAccommodationType(dto.getAccommodationType());
        room.setConfiguration(dto.getConfiguration());
        room.setFurnished(dto.getFurnished());
        room.setAvailableFrom(dto.getAvailableFrom());
        room.setRent(dto.getRent());
        room.setSecurityDeposit(dto.getSecurityDeposit());
        room.setPreferredGender(dto.getPreferredGender());
        room.setIsAvailable(dto.getIsAvailable());
        room.setImages(dto.getImages());
        room.setStatus(dto.getStatus());
        room.setAmenities(dto.getAmenities());
        room.setNoofvacancies(dto.getNoofvacancies());
        room.setCreatedAt(dto.getCreatedAt());
        room.setUpdatedAt(dto.getUpdatedAt());

        // Set user and approvedBy
        room.setUser(user);
        room.setApprovedBy(admin);

        // Map Applications -> Application entity list
        if (dto.getApplications() != null) {
            List<Application> applications = dto.getApplications().stream()
                    .map(appDTO -> ApplicationMapper.toEntity(appDTO, user, room)) // Bi-directional mapping
                    .toList();
            room.setApplications(applications);
        }

        return room;
    }
}
