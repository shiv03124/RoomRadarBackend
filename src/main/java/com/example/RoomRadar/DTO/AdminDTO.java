package com.example.RoomRadar.DTO;

public class AdminDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String password;
    // Constructors
    public AdminDTO() {}

    public AdminDTO(Long id, String fullName, String email, String role, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.password=password;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

