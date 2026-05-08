package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Ad bos olamaz")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Soyad bos olamaz")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "E-posta bos olamaz")
    @Email(message = "Gecerli bir e-posta adresi giriniz")
    private String email;

    @Size(min = 6, message = "Sifre en az 6 karakter olmalidir")
    private String password;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
