package com.storyapi.demo.dto;

public class StoryStatsDTO {
    private long totalStories;
    private long publishedStories;
    private long draftStories;
    private long submittedStories;
    private long archivedStories;

    public StoryStatsDTO(long totalStories, long publishedStories, long draftStories,
            long submittedStories, long archivedStories) {
        this.totalStories = totalStories;
        this.publishedStories = publishedStories;
        this.draftStories = draftStories;
        this.submittedStories = submittedStories;
        this.archivedStories = archivedStories;
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

    public long getSubmittedStories() {
        return submittedStories;
    }

    public long getArchivedStories() {
        return archivedStories;
    }
}

