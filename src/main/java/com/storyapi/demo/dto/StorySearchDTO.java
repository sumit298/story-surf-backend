package com.storyapi.demo.dto;

import java.util.List;

public class StorySearchDTO {
    private List<StoryDTO> stories;
    private String query;
    private List<String> tags;
    private long totalResults;
    
    private int currentPage;
    private int totalPages;
    
    public StorySearchDTO(){}
    
    public StorySearchDTO(List<StoryDTO> stories, String query, List<String> tags, long totalResults, int currentPage, int totalPages) {
        this.stories = stories;
        this.query = query;
        this.tags = tags;
        this.totalResults = totalResults;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public String getQuery() {
        return query;
    }
    
    public List<StoryDTO> getStories() {
        return stories;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public long getTotalResults() {
        return totalResults;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public void setStories(List<StoryDTO> stories) {
        this.stories = stories;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
    
}
