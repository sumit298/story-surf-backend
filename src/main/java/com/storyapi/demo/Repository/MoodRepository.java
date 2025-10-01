package com.storyapi.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.storyapi.demo.Entity.Mood;

public interface MoodRepository extends JpaRepository<Mood, Long>{
    Optional<Mood> findByMoodName(String moodName);
    
    boolean existsByMoodName(String moodName);
    
    
    
    // List<Mood> findByColorCode(String colorCode);
    
    //  @Query("SELECT m.name, COUNT(r) FROM Mood m LEFT JOIN Reflection r ON r.moodReaction = m.name GROUP BY m.name ORDER BY COUNT(r) DESC")
    // List<Object[]> getMoodUsageStats();
    
    // Most popular moods (used in reactions)
    // @Query("SELECT m FROM Mood m WHERE m.name IN (SELECT DISTINCT r.moodReaction FROM Reflection r WHERE r.moodReaction IS NOT NULL) ORDER BY m.displayOrder")
    // List<Mood> findUsedMoods();
    
}
