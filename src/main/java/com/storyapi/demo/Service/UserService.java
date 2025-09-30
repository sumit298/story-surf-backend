package com.storyapi.demo.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;
import com.storyapi.demo.Entity.UserDirectory.UserSettings;
import com.storyapi.demo.Repository.UserRepository;
import com.storyapi.demo.dto.UserRegistrationDTO;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor injection (recommended over field injection)
     * Spring automatically provides these dependencies
     */

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user
     * Business Logic:
     * 1. Check if email already exists
     * 2. Encode password
     * 3. Save user
     * 4. Return created user (without password)
     */
    public User registerUser(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User with email " + registrationDTO.getEmail() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setAge(registrationDTO.getAge());
        user.setBio(registrationDTO.getBio());

        // Encode and set password ONCE
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Initialize settings
        user.setSettings(new UserSettings());
        user.getSettings().setFontSize("medium");
        user.getSettings().setTheme("light");

        // Set default role
        user.setRole(UserRole.READER);

        // Save user
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    /**
     * Find user by email (for authentication)
     */
    @Transactional(readOnly = true)
    public User findById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User email not found"));
    }

    /**
     * Get all users (admin operation)
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get users by role
     */

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Update user profile
     */

    public User updateUserProfile(Long userId, UserRegistrationDTO updateDto) throws ResourceNotFoundException {
        User existingUser = findById(userId);

        if (updateDto.getName() != null) {
            existingUser.setName(updateDto.getName());
        }

        if (updateDto.getAge() != null) {
            existingUser.setAge(updateDto.getAge());

        }

        if (updateDto.getBio() != null) {
            existingUser.setBio(updateDto.getBio());
        }

        return userRepository.save(existingUser);

    }

    public User updateUserSettings(Long userId, String fontSize, String theme) throws ResourceNotFoundException {
        User user = findById(userId);
        if (fontSize != null) {
            user.getSettings().setFontSize(fontSize);

        }

        if (theme != null) {
            user.getSettings().setTheme(theme);
        }

        return userRepository.save(user);
    }

    /**
     * Mark user as applied for author
     */
    public User markAsAppliedForAuthor(Long userId) throws ResourceNotFoundException {
        User user = findById(userId);
        user.setAppliedForAuthor(true);

        return userRepository.save(user);
    }

    // * Promote user to author role
    // * This should typically only be called by admin or approval process
    // */
    public User promoteToAuthor(Long userId) throws ResourceNotFoundException {
        User user = findById(userId);
        user.setRole(UserRole.AUTHOR);
        user.setAppliedForAuthor(true);

        return userRepository.save(user);
    }

    /**
     * Delete user (admin operation)
     */

    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = findById(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // // /**
    // * Get user statistics
    // */
    @Transactional(readOnly = true)
    public UserStats getUserStats() {
        long totalUsers = userRepository.count();
        long readers = userRepository.findByRole(UserRole.READER).size();
        long authors = userRepository.findByRole(UserRole.AUTHOR).size();
        long admins = userRepository.findByRole(UserRole.ADMIN).size();

        return new UserStats(totalUsers, readers, authors, admins);
    }

    public static class UserStats {
        private final long totalUsers;
        private final long readers;
        private final long authors;
        private final long admins;

        public UserStats(long totalUsers, long readers, long authors, long admins) {
            this.totalUsers = totalUsers;
            this.readers = readers;
            this.authors = authors;
            this.admins = admins;
        }

        // Getters
        public long getTotalUsers() {
            return totalUsers;
        }

        public long getReaders() {
            return readers;
        }

        public long getAuthors() {
            return authors;
        }

        public long getAdmins() {
            return admins;
        }
    }

}
