package com.storyapi.demo.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storyapi.demo.Entity.Reflection.ReflectionType;
import com.storyapi.demo.Service.ReflectionService;
import com.storyapi.demo.config.CustomUserDetailsService;
import com.storyapi.demo.config.ResponseUtil;
import com.storyapi.demo.dto.ReflectionCreateDTO;
import com.storyapi.demo.dto.ReflectionDTO;

// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reflection")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReflectionController {
    @Autowired
    private ReflectionService reflectionService;

    /**
     * Create a new reflection (comment or reaction)
     * POST /api/reflections
     * 
     * Body examples:
     * Comment: {"storyId": 1, "type": "COMMENT", "content": "Great story!",
     * "moodReaction": "happy"}
     * Reaction: {"storyId": 1, "type": "REACTION", "moodReaction": "excited"}
     */

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReflection(@Valid @RequestBody ReflectionCreateDTO dto,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            ReflectionDTO reflection = reflectionService.createReflection(dto, userPrincipal.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.success(reflection, "reflection created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/story/{storyId}")
    public ResponseEntity<Map<String, Object>> getStoryReflections(@PathVariable Long storyId) {
        try {
            List<ReflectionDTO> reflections = reflectionService.getStoryReflections(storyId);

            Map<String, Object> responseData = Map.of("reflections", reflections, "total", reflections.size());

            return ResponseEntity.ok(ResponseUtil.success(responseData, "Story Reflections retrieved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/story/{storyId}/type/{type}")
    public ResponseEntity<Map<String, Object>> getStoryReflectionsByType(@PathVariable Long storyId,
            @PathVariable String type) {
        try {
            ReflectionType reflectionType = ReflectionType.valueOf(type.toUpperCase());
            List<ReflectionDTO> reflections = reflectionService.getStoryReflectionsByType(storyId, reflectionType);

            Map<String, Object> responseData = Map.of("reflections", reflections, "type", type, "total",
                    reflections.size());

            return ResponseEntity.ok(ResponseUtil.success(responseData, type.toLowerCase() + "s retried for story"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.error(e.getMessage()));
        }
    }

    @GetMapping("/story/{storyId}/mood-stats")
    public ResponseEntity<Map<String, Object>> getStoryMoodStats(@PathVariable Long storyId) {
        try {
            Map<String, Long> moodStats = reflectionService.getStoryMoodStats(storyId);

            Map<String, Object> responseData = Map.of(
                    "storyId", storyId,
                    "moodStats", moodStats,
                    "totalMoodReactions", moodStats.values().stream().mapToLong(Long::longValue).sum());

            return ResponseEntity.ok(ResponseUtil.success(responseData, "Story mood statistics retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    // @GetMapping("/story/{storyId}/detailed-mood-stats")
    // public ResponseEntity<Map<String, Object>>
    // getDetailedStoryMoodStats(@PathVariable Long storyId) {
    // try {
    // List<StoryMoodStatsDTO> detailedStats =
    // reflectionService.getDetailedStoryMoodStats(storyId);

    // Map<String, Object> responseData = Map.of(
    // "storyId", storyId,
    // "moodBreakdown", detailedStats
    // );

    // return ResponseEntity.ok(ResponseUtil.success(responseData, "Detailed mood
    // statistics retrieved"));

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(ResponseUtil.error(e.getMessage()));
    // }
    // }

    /**
     * Check if current user has reflected on a story
     * GET /api/reflections/story/{storyId}/my-reflection
     */
    @GetMapping("/story/{storyId}/my-reflection")
    public ResponseEntity<Map<String, Object>> getMyReflectionForStory(
            @PathVariable Long storyId,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            Optional<ReflectionDTO> reflection = reflectionService.getUserReflectionForStory(
                    userPrincipal.getId(), storyId);

            Map<String, Object> responseData = Map.of(
                    "hasReflected", reflection.isPresent(),
                    "reflection", reflection.orElse(null));

            return ResponseEntity.ok(ResponseUtil.success(responseData, "User reflection status retrieved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    @PutMapping("/{reflectionId}")
    public ResponseEntity<Map<String, Object>> updateReflection(
            @PathVariable Long reflectionId,
            @RequestBody ReflectionCreateDTO dto,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            ReflectionDTO updatedReflection = reflectionService.updateReflection(
                    reflectionId, dto, userPrincipal.getId());

            return ResponseEntity.ok(ResponseUtil.success(updatedReflection, "Reflection updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    /**
     * Delete user's reflection
     * DELETE /api/reflections/{reflectionId}
     */
    @DeleteMapping("/{reflectionId}")
    public ResponseEntity<Map<String, Object>> deleteReflection(
            @PathVariable Long reflectionId,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();

            reflectionService.deleteReflection(reflectionId, userPrincipal.getId());

            return ResponseEntity.ok(ResponseUtil.success(null, "Reflection deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.error(e.getMessage()));
        }
    }

    // @GetMapping("/my-reflections")
    // public ResponseEntity<Map<String, Object>> getMyReflections(Authentication
    // authentication) {
    // try {
    // CustomUserDetailsService.CustomUserPrincipal userPrincipal =
    // (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();

    // List<ReflectionDTO> reflections =
    // reflectionService.getUserReflections(userPrincipal.getId());

    // Map<String, Object> responseData = Map.of(
    // "reflections", reflections,
    // "total", reflections.size()
    // );

    // return ResponseEntity.ok(ResponseUtil.success(responseData, "Your reflections
    // retrieved"));

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ResponseUtil.error(e.getMessage()));
    // }
    // }

    // @GetMapping("/admin/stats")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<Map<String, Object>> getReflectionStats() {
    // try {
    // ReflectionStatsDTO stats = reflectionService.getReflectionStats();

    // return ResponseEntity.ok(ResponseUtil.success(stats, "Reflection statistics
    // retrieved"));

    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ResponseUtil.error(e.getMessage()));
    // }
    // }
}
