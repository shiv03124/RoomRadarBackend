package com.example.RoomRadar.controller;

import com.example.RoomRadar.DTO.RoomDTO;
import com.example.RoomRadar.Model.RoomStatus;
import com.example.RoomRadar.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Create a new room
    @PostMapping(value = "/add/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomDTO> createRoom(
            @RequestPart("roomDTO") String roomDTOStr,
            @RequestPart("images") MultipartFile[] images,
            @PathVariable Long userId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RoomDTO roomDTO = objectMapper.readValue(roomDTOStr, RoomDTO.class);
        RoomDTO createdRoom = roomService.createRoomWithImages(roomDTO, images, userId);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @GetMapping("/not-applied/{userId}")
    public ResponseEntity<List<RoomDTO>> getRoomsNotAppliedByUser(@PathVariable Long userId) {
        List<RoomDTO> rooms = roomService.getRoomsNotAppliedByUser(userId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/applied/{userId}")
    public ResponseEntity<List<RoomDTO>> getRoomsAppliedByUser(@PathVariable Long userId) {
        List<RoomDTO> rooms = roomService.getAppliedRooms(userId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomDTO>> searchRooms(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "minVacancies", required = false) Integer minVacancies,
            @RequestParam(name = "maxVacancies", required = false) Integer maxVacancies,
            @RequestParam(name = "minRent", required = false) Double minRent,
            @RequestParam(name = "maxRent", required = false) Double maxRent,
            @RequestParam(name = "preferredGender", required = false) String preferredGender,
            @RequestParam(name = "amenities", required = false) List<String> amenities,
            @RequestParam(name = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam(name = "userId", required = false) Long userId
    ) {
        List<RoomDTO> rooms = roomService.searchRooms(city, area, minVacancies, maxVacancies,
                minRent, maxRent, preferredGender,
                amenities, isAvailable, userId);
        return ResponseEntity.ok(rooms);
    }



    @GetMapping("/getRoom/{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }
    @GetMapping("/getAllRoomsWithoutApprove")
    public List<RoomDTO> getAllRoomsWithoutApprove() {
        return roomService.getAllRoomsForAdmin();
    }

    @GetMapping("/approved")
    public ResponseEntity<List<RoomDTO>> getApprovedRooms(
            @RequestParam String accommodationType,
            @RequestParam(required = false) Long userId) {

        List<RoomDTO> rooms;

        if (userId != null) {
            rooms = roomService.getRoomsByTypeExcludingUser(accommodationType, userId);
        } else {
            rooms = roomService.getApprovedRoomsByAccommodationType(accommodationType);
        }

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/type")
    public List<RoomDTO> getRoomsByTypeAndExcludingUser(
            @RequestParam String accommodationType,
            @RequestParam Long userId
    ) {
        return roomService.getRoomsByTypeExcludingUser(accommodationType, userId);
    }


    @PutMapping(value = "/update-with-images/{roomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomDTO> updateRoomWithImages(
            @PathVariable Long roomId,
            @RequestPart("roomDTO") String roomDTOStr,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RoomDTO roomDTO = objectMapper.readValue(roomDTOStr, RoomDTO.class);
        RoomDTO updatedRoom = roomService.updateRoomWithImages(roomId, roomDTO, images);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }

    @GetMapping("/user/{userId}")
    public List<RoomDTO> getRoomsByUserId(@PathVariable Long userId) {
        return roomService.getRoomsByUserId(userId);
    }

    @GetMapping("/notUser/{userId}")
    public List<RoomDTO> getRoomsExcludingUser(@PathVariable Long userId) {
        return roomService.getRoomsExcludingUser(userId);
    }

    @GetMapping("/available-rooms/{userId}")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(@PathVariable Long userId) {
        List<RoomDTO> rooms = roomService.getAvailableRooms(userId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping({"/status", "/status/"})
    public List<RoomDTO> getRoomsByStatus(@RequestParam("status") RoomStatus status) {
        return roomService.getRoomsByStatus(status);
    }

    @PutMapping("/admin/rooms/{roomId}/status")
    public ResponseEntity<RoomDTO> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam String status,
            @RequestParam Long adminId) {

        RoomDTO updatedRoom = roomService.updateRoomStatus(roomId, status, adminId);
        return ResponseEntity.ok(updatedRoom);
    }

}
