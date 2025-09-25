package com.storyapi.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storyapi.demo.Entity.Mood;

public interface MoodRepository extends JpaRepository<Mood, Long>{
    Optional<Mood> findByMoodName(String moodName);
    
    boolean existsByMoodName(String moodName);
    
    List<Mood> findAllByOrderByMoodNameAsc();
}
