package com.storyapi.demo.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.StoryDirectory.Story.StoryStatus;
import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.Entity.UserDirectory.User.UserRole;
import com.storyapi.demo.Repository.StoryRepository;
import com.storyapi.demo.Repository.UserRepository;
import com.storyapi.demo.dto.StoryCreateDTO;
import com.storyapi.demo.dto.StoryDTO;
import com.storyapi.demo.dto.StorySearchDTO;
import com.storyapi.demo.dto.StoryStatsDTO;

import org.springframework.transaction.annotation.Transactional;
import com.storyapi.demo.mapper.DTOMapper;
import jakarta.annotation.Resource;

@Service

public class StoryService {
    @Autowired
    private DTOMapper mapper;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    public StoryDTO createStory(StoryCreateDTO storyDto, Long autherId) throws ResourceNotFoundException {
        User author = userRepository.findById(autherId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        if (author.getRole() != UserRole.AUTHOR && author.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("User is not authorized to create a story");
        }
        // Convert DTO to entity
        Story story = mapper.toStory(storyDto);
        story.setAuthor(author);
        story.setStatus(StoryStatus.DRAFT);

        Story savedStory = storyRepository.save(story);

        return mapper.toStoryDTO(savedStory);
    }

    @Transactional(readOnly = true)
    public StoryDTO getStoryById(Long id) throws ResourceNotFoundException {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id. " +
                        id));

        if (story.getStatus() == StoryStatus.PUBLISHED) {
            story.incrementViews();
            storyRepository.save(story);
        }

        return mapper.toStoryDTO(story);
    }

    @Transactional(readOnly = true)
    public StoryDTO getStoryByIdForEdit(Long id) throws ResourceNotFoundException {
        Story story = storyRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Story not found with id. " +
                        id)));

        return mapper.toStoryDTO(story);
    }

    @Transactional(readOnly = true)
    public Page<StoryDTO> getPublishedStories(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Story> stories = storyRepository.findByStatus(StoryStatus.PUBLISHED, pageable);
        return stories.map(mapper::toStoryDTO);
    }

    @Transactional(readOnly = true)
    public List<StoryDTO> getStories(Long authorId) throws ResourceNotFoundException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        List<Story> stories = storyRepository.findByAuthorOrderByCreatedAtDesc(author);
        return mapper.toStoryDTOList(stories);
    }

    @Transactional(readOnly = true)
    public List<StoryDTO> getStoriesByAutherAndStatus(Long authorId, StoryStatus status)
            throws ResourceNotFoundException {
        User author = userRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException(
                "Author not found"));
        List<Story> stories = storyRepository.findByAuthorAndStatus(author, status);
        return mapper.toStoryDTOList(stories);
    }

    @Transactional(readOnly = true)
    public List<StoryDTO> getTrendingStories(int days, int limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Story> stories = storyRepository.findTrendingStories(StoryStatus.PUBLISHED, since);

        return stories.stream().limit(limit).map(mapper::toStoryDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StoryDTO> getPopularStories(int limit) {
        List<Story> stories = storyRepository.findTop10ByStatusOrderByViewsDesc(StoryStatus.PUBLISHED);
        return stories.stream().limit(limit).map(mapper::toStoryDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<StoryDTO> getLatestStories(int limit) {
        List<Story> stories = storyRepository.findTop10ByStatusOrderByCreatedAtDesc(StoryStatus.PUBLISHED);
        return stories.stream().limit(limit).map(mapper::toStoryDTO).toList();
    }

    // search story by keyword in title and content
    @Transactional(readOnly = true)
    public StorySearchDTO searchStories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Story> titleMatches = storyRepository.findByTitleContainingIgnoreCase(keyword);
        List<Story> contentMatches = storyRepository.searchInTitleAndContent(keyword, StoryStatus.PUBLISHED);

        List<Story> allMatches = titleMatches.stream()
                .filter(story -> story.getStatus() == StoryStatus.PUBLISHED)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        contentMatches.stream()
                .filter(story -> story.getStatus() == StoryStatus.PUBLISHED
                        && !allMatches.contains(story))
                .forEach(allMatches::add);

        List<StoryDTO> storyDTOs = allMatches.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(mapper::toStoryDTO)
                .toList();

        long totalResults = allMatches.size();
        int totalPages = (int) Math.ceil((double) totalResults / size);

        return new StorySearchDTO(storyDTOs, keyword, null, totalResults, page, totalPages);
    }

    // Search stories by tag

    @Transactional(readOnly = true)
    public List<StoryDTO> getStoriesByTag(String tag) {
        List<Story> stories = storyRepository.findByTags(tag);
        return stories.stream().filter(story -> story.getStatus() == StoryStatus.PUBLISHED).map(mapper::toStoryDTO)
                .toList();
    }

    // search story by multipletags
    @Transactional(readOnly = true)
    public List<StoryDTO> getStoriesByTags(List<String> tags) {
        List<Story> stories = storyRepository.findByAllTags(tags, tags.size());
        return stories.stream().filter(story -> story.getStatus() == StoryStatus.PUBLISHED).map(mapper::toStoryDTO)
                .toList();
    }

    public StoryDTO updateStory(Long storyId, StoryCreateDTO updateDto, Long userId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!story.getAuthor().getId().equals(userId) && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("you can only edit your own stories");
        }

        if (story.getStatus() == StoryStatus.PUBLISHED && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("cannot edit published stories");
        }

        mapper.updateStoryFromDTO(story, updateDto);

        Story updatedStory = storyRepository.save(story);
        return mapper.toStoryDTO(updatedStory);

    }

    // public StoryDTO submitStoryForReview(Long storyId, Long authorId) {
    // Story story = storyRepository.findById(storyId)
    // .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

    // // Check ownership
    // if (!story.getAuthor().getId().equals(authorId)) {
    // throw new UnauthorizedException("You can only submit your own stories");
    // }

    // // Can only submit draft stories
    // if (story.getStatus() != StoryStatus.DRAFT) {
    // throw new InvalidRequestException("Only draft stories can be submitted for
    // review");
    // }

    // // Validate story is complete
    // if (story.getTitle() == null || story.getTitle().trim().isEmpty()) {
    // throw new InvalidRequestException("Story must have a title");
    // }
    // if (story.getContent() == null || story.getContent().trim().isEmpty()) {
    // throw new InvalidRequestException("Story must have content");
    // }

    // story.setStatus(StoryStatus.SUBMITTED);
    // Story updatedStory = storyRepository.save(story);

    // return mapper.toStoryDTO(updatedStory);
    // }

    @Transactional
    public StoryDTO approveStory(Long storyId, Long adminId) throws ResourceNotFoundException {
        return updateStoryStatus(storyId, StoryStatus.APPROVED, adminId);
    }

    @Transactional
    public StoryDTO archiveStory(Long storyId, Long adminId) throws ResourceNotFoundException {
        return updateStoryStatus(storyId, StoryStatus.ARCHIVED, adminId);
    }

    // /**
    // * Publish story (admin only)
    // */
    @Transactional
    public StoryDTO publishStory(Long storyId, Long adminId) throws ResourceNotFoundException {
        return updateStoryStatus(storyId, StoryStatus.PUBLISHED, adminId);
    }

    private StoryDTO updateStoryStatus(Long storyId, StoryStatus status, Long adminId)
            throws ResourceNotFoundException {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Only admins can update story status");
        }

        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        story.setStatus(status);
        Story updatedStory = storyRepository.save(story);

        return mapper.toStoryDTO(updatedStory);
    }

    public void deleteStory(Long storyId, Long userId) throws ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!story.getAuthor().getId().equals(userId) && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("You can only delete your own stories");
        }

        if (story.getStatus() == StoryStatus.PUBLISHED && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Cannot delete published Stories");
        }

        storyRepository.delete(story);
    }

    public StoryDTO likeStory(Long storyId) throws InvalidRequestException, ResourceNotFoundException {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story  not found"));

        if (story.getStatus() != StoryStatus.PUBLISHED) {
            throw new InvalidRequestException("Cannot like unpublished stories");

        }

        story.setLikes(story.getLikes() + 1);
        Story updatedStory = storyRepository.save(story);
        return mapper.toStoryDTO(updatedStory);
    }

    // @Transactional(readOnly = true)
    // public List<StoryDTO> getStoriesPendingReview() {
    // List<Story> stories = storyRepository.findByStatus(StoryStatus.SUBMITTED);
    // return mapper.toStoryDTOList(stories);
    // }

    @Transactional(readOnly = true)
    public StoryStatsDTO getStoryStats() {
        long totalStories = storyRepository.count();
        long publishedStories = storyRepository.countByStatus(StoryStatus.PUBLISHED);
        long draftStories = storyRepository.countByStatus(StoryStatus.DRAFT);
        long submittedStories = storyRepository.countByStatus(StoryStatus.SUBMITTED);
        long archivedStories = storyRepository.countByStatus(StoryStatus.ARCHIVED);

        return new StoryStatsDTO(totalStories, publishedStories, draftStories,
                submittedStories, archivedStories);
    }

    // /**
    // * Get author statistics
    // */
    // @Transactional(readOnly = true)
    // public AuthorStatsDTO getAuthorStats(Long authorId) {
    // User author = userRepository.findById(authorId)
    // .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

    // long totalStories = storyRepository.countByAuthor(author);
    // long publishedStories = storyRepository.countByAuthorAndStatus(author,
    // StoryStatus.PUBLISHED);
    // long draftStories = storyRepository.countByAuthorAndStatus(author,
    // StoryStatus.DRAFT);

    // // Calculate total views and likes
    // List<Story> authorStories = storyRepository.findByAuthor(author);
    // long totalViews = authorStories.stream().mapToLong(Story::getViews).sum();
    // long totalLikes = authorStories.stream().mapToLong(Story::getLikes).sum();

    // return new AuthorStatsDTO(totalStories, publishedStories, draftStories,
    // totalViews, totalLikes);
    // }

}


class AuthorStatsDTO {
    private long totalStories;
    private long publishedStories;
    private long draftStories;
    private long totalViews;
    private long totalLikes;

    public AuthorStatsDTO(long totalStories, long publishedStories, long draftStories,
            long totalViews, long totalLikes) {
        this.totalStories = totalStories;
        this.publishedStories = publishedStories;
        this.draftStories = draftStories;
        this.totalViews = totalViews;
        this.totalLikes = totalLikes;
    }

    // Getters
    public long getTotalStories() {
        return totalStories;
    }

    public long getPublishedStories() {
        return publishedStories;
    }

    public long getDraftStories() {
        return draftStories;
    }

    public long getTotalViews() {
        return totalViews;
    }

    public long getTotalLikes() {
        return totalLikes;
    }
}
