package com.storyapi.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.StoryDirectory.Story.StoryStatus;
import com.storyapi.demo.Entity.UserDirectory.User;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByAuthor(User author);

    List<Story> findByAuthorOrderByCreatedAtDesc(User author);

    List<Story> findByAuthorId(Long authorId);

    List<Story> findAllByStatus(StoryStatus status);

    Page<Story> findByStatus(StoryStatus status, Pageable pageable);

    @Query("Select s from Story s JOIN s.tags t where t = :tag")
    List<Story> findByTags(@Param("tag") String tag);

    @Query("Select DISTINCT s from Story s JOIN s.tags t where t IN :tags GROUP BY s having count(t) = :tagcount")
    List<Story> findByAllTags(@Param("tags") List<String> tags, @Param("tagcount") long tagcount);

    List<Story> findByTitleContainingIgnoreCase(String keyword);

    @Query("""
                SELECT s FROM Story s
                WHERE s.status = :status
                  AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(s.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ORDER BY s.createdAt DESC
            """)
    List<Story> searchInTitleAndContent(@Param("keyword") String keyword,
            @Param("status") StoryStatus storyStatus);

    // find popular stories
    List<Story> findTop10ByStatusOrderByViewsDesc(StoryStatus status);

    List<Story> findTop10ByStatusOrderByCreatedAtDesc(StoryStatus status);

    // Custom query to find trending stories (high views in recent time)
    @Query("Select s from Story s where s.status = :status and s.createdAt > :since ORDER BY s.views DESC, s.likes DESC")
    List<Story> findTrendingStories(@Param("status") StoryStatus status, @Param("since") LocalDateTime since);

    long countByAuthor(User author);

    long countByStatus(StoryStatus status);

    List<Story> findByAuthorAndStatus(User author, StoryStatus status);
    
    
    
    
}
