package com.example.nlpEcommerce.dto;

import com.example.nlpEcommerce.model.User;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    public static UserResponse from(User u) {
        UserResponse r = new UserResponse();
        r.id = u.getId();
        r.firstName = u.getFirstName();
        r.lastName = u.getLastName();
        r.email = u.getEmail();
        r.role = u.getRole().name();
        r.createdAt = u.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
