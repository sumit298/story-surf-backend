package com.storyapi.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    
    // check if email exists
    boolean existsByEmail(String email);
    
    // find users by their role
    List<User> findByRole(UserRole role);
    
    // Find users who applied for author role
    List<User> findByAppliedForAuthorTrue();
    
    // find users by age range
    @Query("Select u from user where u.age between :minAge and :maxAge")
    List<User> findUserByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    // find users by specific date
    List<User> findByCreatedAtAfter(LocalDateTime date);
} 