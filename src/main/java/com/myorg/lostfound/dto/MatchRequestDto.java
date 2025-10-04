package com.myorg.lostfound.dto;

import javax.validation.constraints.NotNull;

/**
 * DTO for match requests
 */
public class MatchRequestDto {
    
    @NotNull(message = "Lost item ID is required")
    private Long lostItemId;
    
    private String locationFilter;
    private Integer daysBefore;
    private Integer daysAfter;
    
    public MatchRequestDto() {}
    
    public MatchRequestDto(Long lostItemId) {
        this.lostItemId = lostItemId;
    }
    
    public Long getLostItemId() {
        return lostItemId;
    }
    
    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }
    
    public String getLocationFilter() {
        return locationFilter;
    }
    
    public void setLocationFilter(String locationFilter) {
        this.locationFilter = locationFilter;
    }
    
    public Integer getDaysBefore() {
        return daysBefore;
    }
    
    public void setDaysBefore(Integer daysBefore) {
        this.daysBefore = daysBefore;
    }
    
    public Integer getDaysAfter() {
        return daysAfter;
    }
    
    public void setDaysAfter(Integer daysAfter) {
        this.daysAfter = daysAfter;
    }
}
