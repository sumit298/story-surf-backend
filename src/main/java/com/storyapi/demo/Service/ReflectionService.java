package com.storyapi.demo.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storyapi.demo.Entity.Reflection;
import com.storyapi.demo.Entity.Reflection.ReflectionType;
import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Repository.ReflectionRepository;
import com.storyapi.demo.Repository.StoryRepository;
import com.storyapi.demo.Repository.UserRepository;
import com.storyapi.demo.dto.ReflectionCreateDTO;
import com.storyapi.demo.dto.ReflectionDTO;
import com.storyapi.demo.mapper.DTOMapper;

@Service
public class ReflectionService {

    @Autowired
    private ReflectionRepository reflectionRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DTOMapper mapper;

    public ReflectionDTO createReflection(ReflectionCreateDTO dto, Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Story story = storyRepository.findById(dto.getStoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        if (story.getStatus() != Story.StoryStatus.PUBLISHED) {
            throw new InvalidRequestException("can only reflect on published stories");
        }

        // Check if user already reflected on this story (business rule)
        // if (hasUserAlreadyReflected(userId, dto.getStoryId())) {
        // throw new InvalidRequestException("User has already reflected on this
        // story");
        // }

        Reflection reflection = new Reflection();
        reflection.setStory(story);
        reflection.setUser(user);

        ReflectionType type = ReflectionType.valueOf(dto.getType().toUpperCase());
        reflection.setType(type);

        if (type == ReflectionType.COMMENT && (dto.getContent() == null || dto.getContent().trim().isEmpty())) {
            throw new InvalidRequestException("Comment content is required");
        }

        reflection.setContent(dto.getContent());

        if (dto.getMoodReaction() != null && !dto.getMoodReaction().trim().isEmpty()) {
            reflection.setMoodReaction(dto.getMoodReaction());
        }

        if (type == ReflectionType.REACTION
                && (dto.getMoodReaction() == null || dto.getMoodReaction().trim().isEmpty())) {
            throw new InvalidRequestException("Reflection must have a mood");
        }

        Reflection savedReflection = reflectionRepository.save(reflection);
        story.addReflection(savedReflection);

        return mapper.toReflectionDTO(savedReflection);
    }

    @Transactional(readOnly = true)
    public List<ReflectionDTO> getStoryReflections(Long storyId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));
        List<Reflection> reflections = reflectionRepository.findByStoryOrderByCreatedAtDesc(story);
        return mapper.toReflectionDTOList(reflections);
    }

    @Transactional(readOnly = true)
    public List<ReflectionDTO> getStoryReflectionsByType(Long storyId, ReflectionType type) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));
        List<Reflection> reflections = reflectionRepository.findByStoryAndType(story, type);
        return mapper.toReflectionDTOList(reflections);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getStoryMoodStats(Long storyId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));
        List<Reflection> reflections = reflectionRepository.findByStoryOrderByCreatedAtDesc(story);

        return reflections.stream()
                .filter(r -> r.getMoodReaction() != null && !r.getMoodReaction().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        Reflection::getMoodReaction,
                        Collectors.counting()));
    }

    // @Transactional(readOnly = true)
    // public List<StoryMoodStatsDTO> getDetailedStoryMoodStats(Long storyId) {
    // Map<String, Long> moodCounts = getStoryMoodStats(storyId);

    // return moodCounts.entrySet().stream()
    // .map(entry -> {
    // String moodName = entry.getKey();
    // Long count = entry.getValue();

    // // Get mood details
    // Optional<Mood> moodOpt = moodRepository.findByName(moodName);

    // return new StoryMoodStatsDTO(
    // moodName,
    // count,
    // moodOpt.map(mapper::toMoodDTO).orElse(null)
    // );
    // })
    // .sorted((a, b) -> b.getCount().compareTo(a.getCount())) // Sort by count desc
    // .collect(Collectors.toList());
    // }

    // @Transactional(readOnly = true)
    // public boolean hasUserAlreadyReflected(Long userId, Long storyId) {
    // return reflectionRepository.findByUserIdAndStoryId(userId,
    // storyId).isPresent();
    // }

    @Transactional(readOnly = true)
    public Optional<ReflectionDTO> getUserReflectionForStory(Long userId, Long storyId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));
        List<Reflection> reflections = reflectionRepository.findByStoryOrderByCreatedAtDesc(story);
        Optional<Reflection> reflection = reflections.stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .findFirst();
        return reflection.map(mapper::toReflectionDTO);
    }

    public ReflectionDTO updateReflection(Long reflectionId, ReflectionCreateDTO dto, Long userId)
            throws ResourceNotFoundException {
        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reflection not found"));

        // Check ownership
        if (!reflection.getUser().getId().equals(userId)) {
            throw new InvalidRequestException("Can only update your own reflections");
        }

        // Update content if provided
        if (dto.getContent() != null) {
            reflection.setContent(dto.getContent());
        }

        // Update mood if provided
        if (dto.getMoodReaction() != null) {
            reflection.setMoodReaction(dto.getMoodReaction());
        }

        Reflection updatedReflection = reflectionRepository.save(reflection);
        return mapper.toReflectionDTO(updatedReflection);
    }

    public void deleteReflection(Long reflectionId, Long userId) throws ResourceNotFoundException {
        Reflection reflection = reflectionRepository.findById(reflectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reflection not found"));

        // Check ownership
        if (!reflection.getUser().getId().equals(userId)) {
            throw new InvalidRequestException("Can only delete your own reflections");
        }

        reflectionRepository.delete(reflection);
    }

    /**
     * Get reflection statistics for admin
     */
    // @Transactional(readOnly = true)
    // public ReflectionStatsDTO getReflectionStats() {
    // long totalReflections = reflectionRepository.count();
    // long totalComments =
    // reflectionRepository.countByType(ReflectionType.COMMENT);
    // long totalReactions =
    // reflectionRepository.countByType(ReflectionType.REACTION);

    // Map<String, Long> moodUsageStats = moodService.getMoodUsageStats();

    // return new ReflectionStatsDTO(totalReflections, totalComments,
    // totalReactions, moodUsageStats);
    // }
    // }

    class StoryMoodStatsDTO {
        private String moodName;
        private Long count;
        // private MoodDTO moodDetails;

        public StoryMoodStatsDTO(String moodName, Long count) {
            this.moodName = moodName;
            this.count = count;
            // this.moodDetails = moodDetails;
        }

        // getters and setters
        public String getMoodName() {
            return moodName;
        }

        public Long getCount() {
            return count;
        }
        // public MoodDTO getMoodDetails() { return moodDetails; }
    }

    class ReflectionStatsDTO {
        private long totalReflections;
        private long totalComments;
        private long totalReactions;
        // private Map<String, Long> moodUsageStats;

        public ReflectionStatsDTO(long totalReflections, long totalComments,
                long totalReactions, Map<String, Long> moodUsageStats) {
            this.totalReflections = totalReflections;
            this.totalComments = totalComments;
            this.totalReactions = totalReactions;
            // this.moodUsageStats = moodUsageStats;
        }

        // getters and setters
        public long getTotalReflections() {
            return totalReflections;
        }

        public long getTotalComments() {
            return totalComments;
        }

        public long getTotalReactions() {
            return totalReactions;
        }

        // public Map<String, Long> getMoodUsageStats() {
        //     return moodUsageStats;
        // }
    }
}
