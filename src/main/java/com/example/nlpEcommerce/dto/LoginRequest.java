package com.example.nlpEcommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "E-posta bos olamaz")
    @Email(message = "Gecerli bir e-posta giriniz")
    private String email;

    @NotBlank(message = "Sifre bos olamaz")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
