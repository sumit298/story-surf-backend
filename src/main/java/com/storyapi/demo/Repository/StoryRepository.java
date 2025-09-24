package com.storyapi.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.UserDirectory.User;

public interface StoryRepository extends JpaRepository<Story, Long>{
    List<Story> findByAuthor(User author);
    
    List<Story> findByAuthorId(Long authorId);
    
    // List<Story> findByStatus(StoryStatus status);
}
