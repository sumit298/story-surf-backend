package com.storyapi.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;
import com.storyapi.demo.Service.ResourceNotFoundException;
import com.storyapi.demo.Service.UserService;
import com.storyapi.demo.config.JwtUtil;
import com.storyapi.demo.dto.UserLoginDTO;
import com.storyapi.demo.dto.UserRegistrationDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Debug logging
            System.out.println("[DEBUG] Received registration DTO: " + registrationDTO.getEmail()
                    + ", password present? " + (registrationDTO.getPassword() != null));

            User user = userService.registerUser(registrationDTO); // This will encode password

            // Debug logging
            System.out.println("[DEBUG] User saved: " + user.getEmail() + ", id: " + user.getId());

            response.put("message", "User Registered successfully");
            response.put("user", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("[ERROR] Registration failed: " + e.getMessage());
            e.printStackTrace();

            response.put("error", "Registration failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO)
            throws ResourceNotFoundException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            String token = jwtUtil.generateToken(loginDTO.getEmail());

            User user = userService.findByEmail(loginDTO.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed");
            errorResponse.put("message", "Invalid Email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get user profile");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(Authentication authentication,
            @RequestBody UserRegistrationDTO updateDto) {
        try {
            String email = authentication.getName();
            User currentUser = userService.findByEmail(email);

            if (currentUser == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            User updateUser = userService.updateUserProfile(currentUser.getId(), updateDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile Updated successfully");
            response.put("user", updateUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update profile");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/settings")
    public ResponseEntity<Map<String, Object>> updateSettings(Authentication authentication,
            @RequestBody Map<String, String> settings) {
        try {
            String email = authentication.getName();
            User currentUser = userService.findByEmail(email);

            if (currentUser == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "User  not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            String fontSize = settings.get("fontSize");
            String theme = settings.get("theme");

            User updatedUser = userService.updateUserSettings(currentUser.getId(), fontSize, theme);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Settings updated successfully");
            response.put("user", updatedUser);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "failed to update settings");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(Authentication authentication) {
        try {
            String email = authentication.getName();
            User currentUser = userService.findByEmail(email);

            if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Access denied");
                errorResponse.put("message", "Admin access required");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

            }

            List<User> users = userService.getAllUsers();

            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("total", users.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get Users");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        try {
            UserService.UserStats stats = userService.getUserStats();
            Map<String, Object> response = new HashMap<>();
            response.put("stats", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get user statistics");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }
    
    @PutMapping("/public/{id}/promote-to-author")
    public ResponseEntity<Map<String, Object>> promoteToAuthor(@PathVariable Long id) {
        try {
            User user = userService.promoteToAuthor(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User promoted to author successfully");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to promote user");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
