package com.myorg.lostfound.dto;

import java.time.LocalDateTime;

/**
 * DTO for match results
 */
public class MatchResultDto {
    
    private Long foundItemId;
    private String foundItemTitle;
    private String foundItemDescription;
    private String foundItemCategory;
    private String foundItemLocation;
    private LocalDateTime foundItemDate;
    private String foundItemImagePath;
    private LocalDateTime foundItemCreatedAt;
    
    private String foundByUserName;
    private String foundByUserEmail;
    private String foundByUserPhone;
    
    private Double matchScore;
    private Double textSimilarity;
    private Double locationScore;
    private Double dateScore;
    
    public MatchResultDto() {}
    
    public Long getFoundItemId() {
        return foundItemId;
    }
    
    public void setFoundItemId(Long foundItemId) {
        this.foundItemId = foundItemId;
    }
    
    public String getFoundItemTitle() {
        return foundItemTitle;
    }
    
    public void setFoundItemTitle(String foundItemTitle) {
        this.foundItemTitle = foundItemTitle;
    }
    
    public String getFoundItemDescription() {
        return foundItemDescription;
    }
    
    public void setFoundItemDescription(String foundItemDescription) {
        this.foundItemDescription = foundItemDescription;
    }
    
    public String getFoundItemCategory() {
        return foundItemCategory;
    }
    
    public void setFoundItemCategory(String foundItemCategory) {
        this.foundItemCategory = foundItemCategory;
    }
    
    public String getFoundItemLocation() {
        return foundItemLocation;
    }
    
    public void setFoundItemLocation(String foundItemLocation) {
        this.foundItemLocation = foundItemLocation;
    }
    
    public LocalDateTime getFoundItemDate() {
        return foundItemDate;
    }
    
    public void setFoundItemDate(LocalDateTime foundItemDate) {
        this.foundItemDate = foundItemDate;
    }
    
    public String getFoundItemImagePath() {
        return foundItemImagePath;
    }
    
    public void setFoundItemImagePath(String foundItemImagePath) {
        this.foundItemImagePath = foundItemImagePath;
    }
    
    public LocalDateTime getFoundItemCreatedAt() {
        return foundItemCreatedAt;
    }
    
    public void setFoundItemCreatedAt(LocalDateTime foundItemCreatedAt) {
        this.foundItemCreatedAt = foundItemCreatedAt;
    }
    
    public String getFoundByUserName() {
        return foundByUserName;
    }
    
    public void setFoundByUserName(String foundByUserName) {
        this.foundByUserName = foundByUserName;
    }
    
    public String getFoundByUserEmail() {
        return foundByUserEmail;
    }
    
    public void setFoundByUserEmail(String foundByUserEmail) {
        this.foundByUserEmail = foundByUserEmail;
    }
    
    public String getFoundByUserPhone() {
        return foundByUserPhone;
    }
    
    public void setFoundByUserPhone(String foundByUserPhone) {
        this.foundByUserPhone = foundByUserPhone;
    }
    
    public Double getMatchScore() {
        return matchScore;
    }
    
    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }
    
    public Double getTextSimilarity() {
        return textSimilarity;
    }
    
    public void setTextSimilarity(Double textSimilarity) {
        this.textSimilarity = textSimilarity;
    }
    
    public Double getLocationScore() {
        return locationScore;
    }
    
    public void setLocationScore(Double locationScore) {
        this.locationScore = locationScore;
    }
    
    public Double getDateScore() {
        return dateScore;
    }
    
    public void setDateScore(Double dateScore) {
        this.dateScore = dateScore;
    }
}
