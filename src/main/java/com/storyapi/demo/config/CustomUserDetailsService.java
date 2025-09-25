package com.storyapi.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with email: " + email));

        return new CustomUserPrincipal(user);
    }

    public static class CustomUserPrincipal implements UserDetails {
        private final User user;

        public CustomUserPrincipal(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            return authorities;

        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true; // Account never expires
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // Account is never locked
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // Credentials never expire
        }

        @Override
        public boolean isEnabled() {
            return true; // Account is always enabled
            // You could add a field like user.isActive() here
        }

        /**
         * Get the underlying User entity
         * Useful for accessing additional user information
         */
        public User getUser() {
            return user;
        }

        /**
         * Get user ID
         */
        public Long getId() {
            return user.getId();
        }

        /**
         * Get user display name
         */
        public String getName() {
            return user.getName();
        }

        /**
         * Get user role
         */
        public String getRole() {
            return user.getRole().name();
        }
    }

}
