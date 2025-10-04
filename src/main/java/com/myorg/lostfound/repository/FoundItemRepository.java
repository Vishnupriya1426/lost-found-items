package com.myorg.lostfound.repository;

import com.myorg.lostfound.model.FoundItem;
import com.myorg.lostfound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for FoundItem entity operations
 */
@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {

    /**
     * Find found items by user
     */
    List<FoundItem> findByUser(User user);

    /**
     * Find found items by user ID
     */
    List<FoundItem> findByUserId(Long userId);

    /**
     * Find found items by category
     */
    List<FoundItem> findByCategory(String category);

    /**
     * Find found items by location
     */
    List<FoundItem> findByLocationContainingIgnoreCase(String location);

    /**
     * Find found items by title containing the given string (case insensitive)
     */
    List<FoundItem> findByTitleContainingIgnoreCase(String title);

    /**
     * Find found items found after a specific date
     */
    List<FoundItem> findByDateAfter(LocalDateTime date);

    /**
     * Find found items found before a specific date
     */
    List<FoundItem> findByDateBefore(LocalDateTime date);

    /**
     * Find found items by date range
     */
    List<FoundItem> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find found items by location and date range
     */
    List<FoundItem> findByLocationContainingIgnoreCaseAndDateBetween(String location, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find found items by category and location
     */
    @Query("SELECT fi FROM FoundItem fi WHERE fi.category = :category AND fi.location LIKE %:location%")
    List<FoundItem> findByCategoryAndLocation(@Param("category") String category, @Param("location") String location);

    /**
     * Find recent found items (created within last N days)
     */
    @Query("SELECT fi FROM FoundItem fi WHERE fi.createdAt >= :date")
    List<FoundItem> findRecentFoundItems(@Param("date") LocalDateTime date);

    /**
     * Count found items by user
     */
    long countByUserId(Long userId);

    /**
     * Count found items by category
     */
    long countByCategory(String category);
}
