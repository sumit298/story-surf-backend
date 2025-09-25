package com.storyapi.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.storyapi.demo.Entity.AuthorApplication;
import com.storyapi.demo.Entity.AuthorApplication.ApplicationStatus;
import com.storyapi.demo.Entity.UserDirectory.User;

public interface AuthorApplicationRepository extends JpaRepository<AuthorApplication, Long> {
    List<AuthorApplication> findByUser(User user);

    List<AuthorApplication> findByStatus(ApplicationStatus status);

    List<AuthorApplication> findByStatusOrderByCreatedAtAsc(ApplicationStatus status);

    List<AuthorApplication> findByReviewedBy(User reviewer);

    boolean exexistsByUserAndStatus(User user, ApplicationStatus status);

    Optional<AuthorApplication> findFirstByUserOrderByCreatedAtDesc(User user);

    long countByStatus(ApplicationStatus status);

    @Query("Select a from AuthorApplication a where a.status = 'PENDING' and a.createdAt < :cutoffDate")
    List<AuthorApplication> findStaleApplications(@Param("cutoffDate") LocalDateTime cutoffDate);

}
