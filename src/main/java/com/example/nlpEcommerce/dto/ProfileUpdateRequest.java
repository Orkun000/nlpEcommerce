package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileUpdateRequest {

    @NotBlank(message = "Ad bos olamaz")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Soyad bos olamaz")
    @Size(max = 100)
    private String lastName;

    // Sifre degistirme icin - null ise degistirme
    private String currentPassword;

    @Size(min = 6, message = "Yeni sifre en az 6 karakter olmalidir")
    private String newPassword;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
