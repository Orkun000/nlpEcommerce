package com.example.nlpEcommerce.service.impl;

import com.example.nlpEcommerce.service.*;

import com.example.nlpEcommerce.service.UserService;

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

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return UserResponse.from(findUserOrThrow(id));
    }

    @Transactional
    public UserResponse registerUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(Messages.EMAIL_ALREADY_EXISTS + request.getEmail());
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(PasswordUtils.hash(request.getPassword()));
        user.setRole(User.UserRole.CUSTOMER);
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(Messages.INVALID_EMAIL_OR_PASSWORD));
        if (!PasswordUtils.verify(request.getPassword(), user.getPasswordHash())) {
            throw new ResourceNotFoundException(Messages.INVALID_EMAIL_OR_PASSWORD);
        }
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateProfile(Long id, ProfileUpdateRequest request) {
        User user = findUserOrThrow(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null ||
                    !PasswordUtils.verify(request.getCurrentPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException(Messages.INVALID_CURRENT_PASSWORD);
            }
            user.setPasswordHash(PasswordUtils.hash(request.getNewPassword()));
        }
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        findUserOrThrow(id);
        userRepository.deleteById(id);
    }

    public User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici", id));
    }
}
