package com.storyapi.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storyapi.demo.Service.MoodService;
import com.storyapi.demo.Service.ResourceNotFoundException;
import com.storyapi.demo.config.ResponseUtil;
import com.storyapi.demo.dto.MoodDTO;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MoodController {
    @Autowired
    private MoodService moodService;

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getMoodByMoodName(@PathVariable String name)
            throws ResourceNotFoundException {
        MoodDTO mood = moodService.findByName(name);
        return ResponseEntity.ok(ResponseUtil.success(mood, "Mood retrieved"));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createMood(@RequestBody MoodCreateDTO createDTO) {
        MoodDTO mood = moodService.createMood(
                createDTO.getName(),
                createDTO.getColorCode()

        );

        return ResponseEntity.ok(ResponseUtil.success(mood, "Mood created successfully"));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateMood(
            @PathVariable Long id,
            @RequestBody MoodUpdateDTO updateDTO) throws ResourceNotFoundException {

        MoodDTO mood = moodService.updateMood(
                id,
                updateDTO.getName(),
                updateDTO.getColorCode());

        return ResponseEntity.ok(ResponseUtil.success(mood, "Mood updated successfully"));
    }

    public class MoodCreateDTO {
        private String name;
        private String colorCode;

        public String getColorCode() {
            return colorCode;
        }

        public String getName() {
            return name;
        }

        public void setColorCode(String colorCode) {
            this.colorCode = colorCode;
        }

        public void setName(String name) {
            this.name = name;
        }

        // getters and setters
    }

    public class MoodUpdateDTO {
        private String colorCode;
        private String name;

        public String getName() {
            return name;
        }

        public String getColorCode() {
            return colorCode;
        }
        // getters and setters
    }
}
