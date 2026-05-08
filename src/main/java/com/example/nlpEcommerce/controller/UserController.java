package com.example.nlpEcommerce.controller;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.*;
import com.example.nlpEcommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Kullanici yonetimi")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Tum kullanicilari listele (Admin)")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kullanici profili")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PostMapping("/register")
    @Operation(summary = "Yeni kullanici kaydi")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Messages.USER_REGISTERED_SUCCESS, userService.registerUser(request)));
    }

    @PostMapping("/login")
    @Operation(summary = "Giris yap")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(Messages.USER_LOGIN_SUCCESS, userService.login(request)));
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Profil guncelle")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long id, @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity
                .ok(ApiResponse.success(Messages.USER_PROFILE_UPDATED, userService.updateProfile(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kullanici sil (Admin)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(Messages.USER_DELETED, null));
    }
}
