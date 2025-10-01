package com.storyapi.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.storyapi.demo.Entity.Reflection;
import com.storyapi.demo.Entity.Reflection.ReflectionType;

import java.time.LocalDateTime;
import java.util.List;
import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.UserDirectory.User;

public interface ReflectionRepository extends JpaRepository<Reflection, Long> {
    List<Reflection> findByStory(Story story);

    // Find reflections for a story ordered by creation date
    List<Reflection> findByStoryOrderByCreatedAtDesc(Story story);

    List<Reflection> findByUser(User user);

    List<Reflection> findByType(ReflectionType type);

    List<Reflection> findByStoryAndType(Story story, ReflectionType type);

    long countByStory(Story story);

    long countByStoryAndType(Story story, ReflectionType type);

    // Find recent reflections across all stories
    @Query("Select r from Reflection r where r.createdAt > :since order by r.createdAt DESC")
    List<Reflection> findRecentReflections(@Param("since") LocalDateTime since);

}
