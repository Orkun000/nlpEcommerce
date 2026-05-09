package com.example.nlpEcommerce.service;

import com.example.nlpEcommerce.constant.Messages;
import com.example.nlpEcommerce.dto.LoginRequest;
import com.example.nlpEcommerce.dto.ProfileUpdateRequest;
import com.example.nlpEcommerce.dto.UserRequest;
import com.example.nlpEcommerce.dto.UserResponse;
import com.example.nlpEcommerce.exception.DuplicateResourceException;
import com.example.nlpEcommerce.exception.ResourceNotFoundException;
import com.example.nlpEcommerce.model.User;
import com.example.nlpEcommerce.repository.UserRepository;
import com.example.nlpEcommerce.util.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public interface UserService {
    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(Long id);
    public UserResponse registerUser(UserRequest request);
    public UserResponse login(LoginRequest request);
    public UserResponse updateProfile(Long id, ProfileUpdateRequest request);
    public void deleteUser(Long id);
    public User findUserOrThrow(Long id);
}
