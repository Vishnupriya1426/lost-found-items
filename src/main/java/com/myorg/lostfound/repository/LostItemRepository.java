package com.myorg.lostfound.repository;

import com.myorg.lostfound.model.LostItem;
import com.myorg.lostfound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for LostItem entity operations
 */
@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    /**
     * Find lost items by user
     */
    List<LostItem> findByUser(User user);

    /**
     * Find lost items by user ID
     */
    List<LostItem> findByUserId(Long userId);

    /**
     * Find lost items by category
     */
    List<LostItem> findByCategory(String category);

    /**
     * Find lost items by location
     */
    List<LostItem> findByLocationContainingIgnoreCase(String location);

    /**
     * Find lost items by title containing the given string (case insensitive)
     */
    List<LostItem> findByTitleContainingIgnoreCase(String title);

    /**
     * Find lost items lost after a specific date
     */
    List<LostItem> findByDateAfter(LocalDateTime date);

    /**
     * Find lost items lost before a specific date
     */
    List<LostItem> findByDateBefore(LocalDateTime date);

    /**
     * Find lost items by category and location
     */
    @Query("SELECT li FROM LostItem li WHERE li.category = :category AND li.location LIKE %:location%")
    List<LostItem> findByCategoryAndLocation(@Param("category") String category, @Param("location") String location);

    /**
     * Find recent lost items (created within last N days)
     */
    @Query("SELECT li FROM LostItem li WHERE li.createdAt >= :date")
    List<LostItem> findRecentLostItems(@Param("date") LocalDateTime date);

    /**
     * Count lost items by user
     */
    long countByUserId(Long userId);

    /**
     * Count lost items by category
     */
    long countByCategory(String category);
}
