package com.example.RoomRadar.mapper;

import com.example.RoomRadar.DTO.ApplicationDTO;
import com.example.RoomRadar.Model.Application;
import com.example.RoomRadar.Model.Room;
import com.example.RoomRadar.Model.User;

public class ApplicationMapper {

    public static ApplicationDTO toDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setStatus(app.getStatus());
        dto.setAppliedAt(app.getAppliedAt());
        dto.setApplicantId(app.getApplicant() != null ? app.getApplicant().getId() : null);
        dto.setRoomId(app.getRoom() != null ? app.getRoom().getId() : null);
        dto.setMessage(app.getMessage());
        return dto;
    }

    public static Application toEntity(ApplicationDTO dto, User user, Room room) {
        Application app = new Application();
        app.setId(dto.getId());
        app.setStatus(dto.getStatus());
        app.setAppliedAt(dto.getAppliedAt());
        app.setApplicant(user);
        app.setRoom(room);
        app.setMessage(dto.getMessage());
        return app;
    }
}
