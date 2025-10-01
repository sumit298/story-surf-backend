package com.storyapi.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storyapi.demo.Entity.StoryDirectory.Story.StoryStatus;
import com.storyapi.demo.Service.StoryService;
import com.storyapi.demo.config.CustomUserDetailsService;
import com.storyapi.demo.config.ResponseUtil;
import com.storyapi.demo.dto.StoryCreateDTO;
import com.storyapi.demo.dto.StoryDTO;
import org.springframework.security.core.Authentication;

import com.storyapi.demo.dto.StorySearchDTO;
import com.storyapi.demo.dto.StoryStatsDTO;

@RestController
@RequestMapping("api/stories")
@CrossOrigin(origins = "*", maxAge = 3600)


public class StoryController {

    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private StoryService storyService;

    StoryController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublishedStories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            Page<StoryDTO> stories = storyService.getPublishedStories(page, size, sortBy);

            Map<String, Object> response = Map.of("stories", stories.getContent(),
                    "currentPage", stories.getNumber(), "totalPages", stories.getTotalPages(), "totalElements",
                    stories.getTotalElements(), "hasNext", stories.hasNext(), "hasPrevious", stories.hasPrevious());

            return ResponseEntity.ok(ResponseUtil.success(response, "Published Stories retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/trending")
    public ResponseEntity<Map<String, Object>> getTrendingStories(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<StoryDTO> stories = storyService.getTrendingStories(days, limit);

            Map<String, Object> response = Map.of(
                    "stories", stories,
                    "period", days + " days",
                    "total", stories.size());

            return ResponseEntity.ok(ResponseUtil.success(response, "Trending stories retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/popular")
    public ResponseEntity<Map<String, Object>> getPopularStories(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<StoryDTO> stories = storyService.getPopularStories(limit);

            Map<String, Object> response = Map.of(
                    "stories", stories,
                    "total", stories.size());

            return ResponseEntity.ok(ResponseUtil.success(response, "Popular stories retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/latest")
    public ResponseEntity<Map<String, Object>> getLatestStories(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<StoryDTO> stories = storyService.getLatestStories(limit);

            Map<String, Object> response = Map.of(
                    "stories", stories,
                    "total", stories.size());

            return ResponseEntity.ok(ResponseUtil.success(response, "Latest stories retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Map<String, Object>> getStoryById(@PathVariable Long id) {
        try {
            StoryDTO story = storyService.getStoryById(id);
            return ResponseEntity.ok(ResponseUtil.success(story, "Story retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/search")
    public ResponseEntity<Map<String, Object>> searchStories(@RequestParam String q,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            StorySearchDTO searchResult = storyService.searchStories(q, page, size);
            return ResponseEntity.ok(ResponseUtil.success(searchResult, "search completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/tag/{tag}")
    public ResponseEntity<Map<String, Object>> getStoriesByTag(@PathVariable String tag) {
        try {
            List<StoryDTO> stories = storyService.getStoriesByTag(tag);
            Map<String, Object> response = Map.of("stories", stories, "tag", tag, "total", stories.size());

            return ResponseEntity.ok(ResponseUtil.success(response, "Stories by tags fetched"));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/public/author/{authorId}")
    public ResponseEntity<Map<String, Object>> getStoriesByAuthorAndStatus(@PathVariable Long authorId,
            @RequestParam String status) {
        try {
            StoryStatus storyStatus = StoryStatus.valueOf(status.toUpperCase());
            List<StoryDTO> stories = storyService.getStoriesByAutherAndStatus(authorId, storyStatus);
            Map<String, Object> response = Map.of(
                    "stories", stories,
                    "authorId", authorId,
                    "status", status,
                    "total", stories.size());
            return ResponseEntity.ok(ResponseUtil.success(response, "Stories by author and status retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> likeStory(@PathVariable Long id, Authentication authentication){
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            StoryDTO story = storyService.likeStory(id, userPrincipal.getId());
            
            Map<String, Object> response = Map.of("story", story, "message", "Story liked successfully");
            
            return ResponseEntity.ok(ResponseUtil.success(response, "Story liked successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStory(@RequestBody StoryCreateDTO storyDTO, Authentication authentication ){
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            StoryDTO createdStory = storyService.createStory(storyDTO, principal.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtil.success(createdStory, "Story created successfully"));
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/my-stories")
    public ResponseEntity<Map<String, Object>> getMyStories(Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            List<StoryDTO> stories = storyService.getStories(principal.getId());
            
            Map<String, Object> response = Map.of("stories", stories, "total", stories.size());
            return ResponseEntity.ok(ResponseUtil.success(response, "User stories retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/edit")
    public ResponseEntity<Map<String, Object>> getStoryForEdit(@PathVariable Long id, Authentication authentication) {
        try {
            StoryDTO story = storyService.getStoryByIdForEdit(id);
            return ResponseEntity.ok(ResponseUtil.success(story, "Story retrieved for editing"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStory(@PathVariable Long id, @RequestBody StoryCreateDTO updateDto, Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            StoryDTO updatedStory = storyService.updateStory(id, updateDto, principal.getId());
            
            return ResponseEntity.ok(ResponseUtil.success(updatedStory, "Story updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStory(@PathVariable Long id, Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            storyService.deleteStory(id, principal.getId());
            
            return ResponseEntity.ok(ResponseUtil.success(null, "Story deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error(e.getMessage()));
        }
    }
    
    // @PostMapping("/{id}/save")

    // @GetMapping("/admin/pending")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<Map<String, Object>> getPendingStories() {
    // try {
    // List<StoryDTO> stories = storyService.getStoriesPendingReview();

    // Map<String, Object> response = Map.of(
    // "stories", stories,
    // "total", stories.size()
    // );

    // return ResponseEntity.ok(ResponseUtil.success(response, "Pending stories
    // retrieved"));

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ResponseUtil.error(e.getMessage()));
    // }
    // }

    @PutMapping("/admin/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> approveStory(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            StoryDTO story = storyService.approveStory(id, userPrincipal.getId());

            return ResponseEntity.ok(ResponseUtil.success(story, "Story approved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * Publish story
     * PUT /api/stories/admin/{id}/publish
     */
    @PutMapping("/admin/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> publishStory(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            StoryDTO story = storyService.publishStory(id, userPrincipal.getId());

            return ResponseEntity.ok(ResponseUtil.success(story, "Story published"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    // @PutMapping("/admin/{id}/reject")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<Map<String, Object>> rejectStory(
    // @PathVariable Long id,
    // Authentication authentication) {
    // try {
    // CustomUserDetailsService.CustomUserPrincipal userPrincipal =
    // (CustomUserDetailsService.CustomUserPrincipal) authentication
    // .getPrincipal();

    // StoryDTO story = storyService.rejectStory(id, userPrincipal.getId());

    // return ResponseEntity.ok(ResponseUtil.success(story, "Story rejected"));

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    // .body(ResponseUtil.error(e.getMessage()));
    // }
    // }

    @PutMapping("/admin/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> archiveStory(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            StoryDTO story = storyService.archiveStory(id, userPrincipal.getId());

            return ResponseEntity.ok(ResponseUtil.success(story, "Story archived"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStoryStats() {
        try {
            StoryStatsDTO stats = storyService.getStoryStats();

            return ResponseEntity.ok(ResponseUtil.success(stats, "Story statistics retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

}
