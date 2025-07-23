package com.example.RoomRadar.DTO;

import com.example.RoomRadar.Model.RoomStatus;
import com.example.RoomRadar.Model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RoomDTO {

    private Long id;
    private String title;
    private String description;
    private String address;
    private String city;
    private String area;
    private String accommodationType;
    private RoomStatus status;
    private String configuration;
    private String furnished;
    private LocalDate availableFrom;
    private Double rent;
    private int totalNoOfPeoples;
    private Double securityDeposit;
    private String preferredGender;
    private Boolean isAvailable;
    private List<String> images;
    private List<String> amenities;

    private UserDTO user;
    private AdminDTO approvedBy;
    private int noofvacancies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ApplicationDTO> applications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFurnished() {
        return furnished;
    }

    public void setFurnished(String furnished) {
        this.furnished = furnished;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ApplicationDTO> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationDTO> applications) {
        this.applications = applications;
    }

    public int getNoofvacancies() {
        return noofvacancies;
    }

    public void setNoofvacancies(int noofvacancies) {
        this.noofvacancies = noofvacancies;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }


    public AdminDTO getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(AdminDTO approvedBy) {
        this.approvedBy = approvedBy;
    }


    public int getTotalNoOfPeoples() {
        return totalNoOfPeoples;
    }

    public void setTotalNoOfPeoples(int totalNoOfPeoples) {
        this.totalNoOfPeoples = totalNoOfPeoples;
    }
}
