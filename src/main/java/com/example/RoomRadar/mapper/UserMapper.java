package com.example.RoomRadar.mapper;

import com.example.RoomRadar.DTO.ApplicationDTO;
import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.DTO.UserDTO;
import com.example.RoomRadar.Model.Application;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());
        dto.setCurrentCity(user.getCurrentCity());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setPassword(user.getPassword());

        // Map preferences list
        if (user.getPreferences() != null) {
            dto.setPreferences(user.getPreferences());
        }

        if (user.getRooms() != null) {
            List<RoomDTO> roomDTOs = user.getRooms()
                    .stream()
                    .map(RoomMapper::toDTO)
                    .toList();
            dto.setRooms(roomDTOs);
        }

        if (user.getApplications() != null) {
            dto.setApplications(user.getApplications().stream()
                    .map(ApplicationMapper::toDTO)
                    .toList());
        }

        return dto;
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setCurrentCity(dto.getCurrentCity());
        user.setProfileImageUrl(dto.getProfileImageUrl());
        user.setPassword(dto.getPassword());
        user.setPreferences(dto.getPreferences());

        // Map preferences list
        if (dto.getPreferences() != null) {
            user.setPreferences(dto.getPreferences());
        }

        List<Room> rooms = null;
        if (dto.getRooms() != null) {
            rooms = dto.getRooms().stream()
                    .map(roomDTO -> RoomMapper.toEntity(roomDTO, user, null)) // bi-directional mapping
                    .toList();
            user.setRooms(rooms);
        }

        if (dto.getApplications() != null) {
            // Map roomId to Room entity for fast lookup if needed
            Map<Long, Room> roomMap = rooms != null ? rooms.stream()
                    .filter(room -> room.getId() != null)
                    .collect(Collectors.toMap(Room::getId, Function.identity())) : Map.of();

            List<Application> applications = dto.getApplications().stream()
                    .map(appDTO -> {
                        Room targetRoom = roomMap.get(appDTO.getRoomId());
                        return ApplicationMapper.toEntity(appDTO, user, targetRoom);
                    })
                    .toList();

            user.setApplications(applications);
        }

        return user;
    }
}
