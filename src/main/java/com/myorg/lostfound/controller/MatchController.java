package com.myorg.lostfound.controller;

import com.myorg.lostfound.dto.MatchRequestDto;
import com.myorg.lostfound.dto.MatchResultDto;
import com.myorg.lostfound.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for match operations
 */
@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * Find matches for a lost item
     */
    @PostMapping("/find")
    public ResponseEntity<Map<String, Object>> findMatches(@Valid @RequestBody MatchRequestDto request) {
        try {
            List<MatchResultDto> matches = matchService.findMatches(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Matches found successfully");
            response.put("data", matches);
            response.put("count", matches.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error finding matches: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Find matches for a lost item by ID (simple endpoint)
     */
    @GetMapping("/lost-item/{lostItemId}")
    public ResponseEntity<Map<String, Object>> findMatchesByLostItemId(@PathVariable Long lostItemId) {
        try {
            MatchRequestDto request = new MatchRequestDto(lostItemId);
            List<MatchResultDto> matches = matchService.findMatches(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Matches found successfully");
            response.put("data", matches);
            response.put("count", matches.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error finding matches: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Find matches with location filter
     */
    @GetMapping("/lost-item/{lostItemId}/location/{location}")
    public ResponseEntity<Map<String, Object>> findMatchesByLocation(
            @PathVariable Long lostItemId, 
            @PathVariable String location) {
        try {
            MatchRequestDto request = new MatchRequestDto(lostItemId);
            request.setLocationFilter(location);
            List<MatchResultDto> matches = matchService.findMatches(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Matches found successfully");
            response.put("data", matches);
            response.put("count", matches.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error finding matches: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Find matches with date range filter
     */
    @GetMapping("/lost-item/{lostItemId}/date-range")
    public ResponseEntity<Map<String, Object>> findMatchesByDateRange(
            @PathVariable Long lostItemId,
            @RequestParam(required = false) Integer daysBefore,
            @RequestParam(required = false) Integer daysAfter) {
        try {
            MatchRequestDto request = new MatchRequestDto(lostItemId);
            request.setDaysBefore(daysBefore);
            request.setDaysAfter(daysAfter);
            List<MatchResultDto> matches = matchService.findMatches(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Matches found successfully");
            response.put("data", matches);
            response.put("count", matches.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error finding matches: " + e.getMessage());
            response.put("data", null);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
