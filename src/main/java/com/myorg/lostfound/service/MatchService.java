package com.myorg.lostfound.service;

import com.myorg.lostfound.dto.MatchRequestDto;
import com.myorg.lostfound.dto.MatchResultDto;
import com.myorg.lostfound.model.LostItem;
import com.myorg.lostfound.model.FoundItem;
import com.myorg.lostfound.repository.LostItemRepository;
import com.myorg.lostfound.repository.FoundItemRepository;
import com.myorg.lostfound.util.TextVectorizer;
import com.myorg.lostfound.util.CosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for finding matches between lost and found items
 */
@Service
public class MatchService {
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    @Autowired
    private FoundItemRepository foundItemRepository;
    
    private TextVectorizer textVectorizer;
    private boolean vectorizerInitialized = false;
    
    /**
     * Find matches for a given lost item
     */
    public List<MatchResultDto> findMatches(MatchRequestDto request) {
        // Get the lost item
        Optional<LostItem> lostItemOpt = lostItemRepository.findById(request.getLostItemId());
        if (!lostItemOpt.isPresent()) {
            return Collections.emptyList();
        }
        
        LostItem lostItem = lostItemOpt.get();
        
        // Initialize vectorizer if not already done
        initializeVectorizer();
        
        // Get candidate found items based on filters
        List<FoundItem> candidates = getCandidateFoundItems(lostItem, request);
        
        // Calculate match scores and create results
        List<MatchResultDto> matches = candidates.stream()
                .map(foundItem -> calculateMatch(lostItem, foundItem))
                .filter(match -> match.getMatchScore() > 0.0) // Only include matches with score > 0
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore())) // Sort by score descending
                .limit(10) // Top 10 matches
                .collect(Collectors.toList());
        
        return matches;
    }
    
    /**
     * Initialize the text vectorizer with all descriptions from the database
     */
    private void initializeVectorizer() {
        if (vectorizerInitialized) {
            return;
        }
        
        try {
            // Get all descriptions from lost and found items
            List<String> allDescriptions = new ArrayList<>();
            
            // Add lost item descriptions
            List<LostItem> allLostItems = lostItemRepository.findAll();
            for (LostItem item : allLostItems) {
                if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
                    allDescriptions.add(item.getDescription());
                }
            }
            
            // Add found item descriptions
            List<FoundItem> allFoundItems = foundItemRepository.findAll();
            for (FoundItem item : allFoundItems) {
                if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
                    allDescriptions.add(item.getDescription());
                }
            }
            
            // Initialize vectorizer if we have descriptions
            if (!allDescriptions.isEmpty()) {
                textVectorizer = new TextVectorizer();
                textVectorizer.fit(allDescriptions);
                vectorizerInitialized = true;
            }
        } catch (Exception e) {
            // If initialization fails, we'll fall back to simple text similarity
            System.err.println("Failed to initialize text vectorizer: " + e.getMessage());
        }
    }
    
    /**
     * Get candidate found items based on location and date filters
     */
    private List<FoundItem> getCandidateFoundItems(LostItem lostItem, MatchRequestDto request) {
        LocalDateTime lostItemDate = lostItem.getDate();
        
        // Calculate date range
        LocalDateTime startDate = lostItemDate;
        LocalDateTime endDate = lostItemDate;
        
        if (request.getDaysBefore() != null) {
            startDate = lostItemDate.minusDays(request.getDaysBefore());
        }
        if (request.getDaysAfter() != null) {
            endDate = lostItemDate.plusDays(request.getDaysAfter());
        }
        
        // Get candidates based on filters
        if (request.getLocationFilter() != null && !request.getLocationFilter().trim().isEmpty()) {
            // Filter by location and date range
            return foundItemRepository.findByLocationContainingIgnoreCaseAndDateBetween(
                    request.getLocationFilter(), startDate, endDate);
        } else {
            // Filter only by date range
            return foundItemRepository.findByDateBetween(startDate, endDate);
        }
    }
    
    /**
     * Calculate match score between lost and found items
     */
    private MatchResultDto calculateMatch(LostItem lostItem, FoundItem foundItem) {
        MatchResultDto result = new MatchResultDto();
        
        // Set found item details
        result.setFoundItemId(foundItem.getId());
        result.setFoundItemTitle(foundItem.getTitle());
        result.setFoundItemDescription(foundItem.getDescription());
        result.setFoundItemCategory(foundItem.getCategory());
        result.setFoundItemLocation(foundItem.getLocation());
        result.setFoundItemDate(foundItem.getDate());
        result.setFoundItemImagePath(foundItem.getImagePath());
        result.setFoundItemCreatedAt(foundItem.getCreatedAt());
        
        // Set user details
        if (foundItem.getUser() != null) {
            result.setFoundByUserName(foundItem.getUser().getName());
            result.setFoundByUserEmail(foundItem.getUser().getEmail());
            result.setFoundByUserPhone(foundItem.getUser().getPhone());
        }
        
        // Calculate scores
        double textSimilarity = calculateTextSimilarity(lostItem.getDescription(), foundItem.getDescription());
        double locationScore = calculateLocationScore(lostItem.getLocation(), foundItem.getLocation());
        double dateScore = calculateDateScore(lostItem.getDate(), foundItem.getDate());
        
        // Calculate overall match score: 0.6 * textSimilarity + 0.3 * locationScore + 0.1 * dateScore
        double matchScore = 0.6 * textSimilarity + 0.3 * locationScore + 0.1 * dateScore;
        
        result.setTextSimilarity(textSimilarity);
        result.setLocationScore(locationScore);
        result.setDateScore(dateScore);
        result.setMatchScore(matchScore);
        
        return result;
    }
    
    /**
     * Calculate text similarity between two descriptions using TF-IDF and cosine similarity
     */
    private double calculateTextSimilarity(String description1, String description2) {
        if (description1 == null || description2 == null) {
            return 0.0;
        }
        
        // If vectorizer is not initialized, fall back to simple Jaccard similarity
        if (!vectorizerInitialized || textVectorizer == null) {
            return calculateSimpleTextSimilarity(description1, description2);
        }
        
        try {
            // Transform descriptions to TF-IDF vectors
            Map<String, Double> vector1 = textVectorizer.transform(description1);
            Map<String, Double> vector2 = textVectorizer.transform(description2);
            
            // Calculate cosine similarity
            return CosineSimilarity.compute(vector1, vector2);
        } catch (Exception e) {
            // Fall back to simple similarity if TF-IDF fails
            System.err.println("TF-IDF calculation failed, falling back to simple similarity: " + e.getMessage());
            return calculateSimpleTextSimilarity(description1, description2);
        }
    }
    
    /**
     * Fallback method for simple text similarity (Jaccard similarity)
     */
    private double calculateSimpleTextSimilarity(String description1, String description2) {
        if (description1 == null || description2 == null) {
            return 0.0;
        }
        
        // Tokenize and normalize text
        Set<String> words1 = tokenizeAndNormalize(description1);
        Set<String> words2 = tokenizeAndNormalize(description2);
        
        if (words1.isEmpty() && words2.isEmpty()) {
            return 1.0;
        }
        if (words1.isEmpty() || words2.isEmpty()) {
            return 0.0;
        }
        
        // Calculate Jaccard similarity
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Tokenize and normalize text (lowercase, remove punctuation)
     */
    private Set<String> tokenizeAndNormalize(String text) {
        return Arrays.stream(text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", " ") // Replace punctuation with spaces
                .split("\\s+"))
                .filter(word -> word.length() > 2) // Filter out short words
                .collect(Collectors.toSet());
    }
    
    /**
     * Calculate location similarity score
     */
    private double calculateLocationScore(String location1, String location2) {
        if (location1 == null || location2 == null) {
            return 0.0;
        }
        
        String loc1 = location1.toLowerCase().trim();
        String loc2 = location2.toLowerCase().trim();
        
        // Exact match
        if (loc1.equals(loc2)) {
            return 1.0;
        }
        
        // Check if one location contains the other
        if (loc1.contains(loc2) || loc2.contains(loc1)) {
            return 0.8;
        }
        
        // Calculate word overlap
        Set<String> words1 = new HashSet<>(Arrays.asList(loc1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(loc2.split("\\s+")));
        
        if (words1.isEmpty() && words2.isEmpty()) {
            return 1.0;
        }
        if (words1.isEmpty() || words2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Calculate date similarity score
     */
    private double calculateDateScore(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            return 0.0;
        }
        
        // Calculate difference in days
        long daysDifference = Math.abs(java.time.Duration.between(date1, date2).toDays());
        
        // Score decreases as days difference increases
        // Perfect match (0 days) = 1.0
        // 1 day difference = 0.9
        // 7 days difference = 0.3
        // 30+ days difference = 0.0
        if (daysDifference == 0) {
            return 1.0;
        } else if (daysDifference <= 1) {
            return 0.9;
        } else if (daysDifference <= 3) {
            return 0.7;
        } else if (daysDifference <= 7) {
            return 0.5;
        } else if (daysDifference <= 14) {
            return 0.3;
        } else if (daysDifference <= 30) {
            return 0.1;
        } else {
            return 0.0;
        }
    }
}
