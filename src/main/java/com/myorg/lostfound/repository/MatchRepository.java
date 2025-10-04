package com.myorg.lostfound.repository;

import com.myorg.lostfound.model.FoundItem;
import com.myorg.lostfound.model.LostItem;
import com.myorg.lostfound.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Match entity operations
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    /**
     * Find matches by lost item
     */
    List<Match> findByLostItem(LostItem lostItem);

    /**
     * Find matches by found item
     */
    List<Match> findByFoundItem(FoundItem foundItem);

    /**
     * Find matches by lost item ID
     */
    List<Match> findByLostItemId(Long lostItemId);

    /**
     * Find matches by found item ID
     */
    List<Match> findByFoundItemId(Long foundItemId);

    /**
     * Find matches by status
     */
    List<Match> findByStatus(Match.MatchStatus status);

    /**
     * Find match by lost item and found item
     */
    Optional<Match> findByLostItemAndFoundItem(LostItem lostItem, FoundItem foundItem);

    /**
     * Find matches created after a specific date
     */
    List<Match> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find matches with score greater than or equal to given score
     */
    @Query("SELECT m FROM Match m WHERE m.matchScore >= :score")
    List<Match> findByMatchScoreGreaterThanEqual(@Param("score") Double score);

    /**
     * Find pending matches
     */
    @Query("SELECT m FROM Match m WHERE m.status = 'PENDING'")
    List<Match> findPendingMatches();

    /**
     * Find confirmed matches
     */
    @Query("SELECT m FROM Match m WHERE m.status = 'CONFIRMED'")
    List<Match> findConfirmedMatches();

    /**
     * Count matches by status
     */
    long countByStatus(Match.MatchStatus status);

    /**
     * Count matches by lost item
     */
    long countByLostItemId(Long lostItemId);

    /**
     * Count matches by found item
     */
    long countByFoundItemId(Long foundItemId);

    /**
     * Find matches for a specific user (through lost items)
     */
    @Query("SELECT m FROM Match m JOIN m.lostItem li WHERE li.user.id = :userId")
    List<Match> findMatchesForLostItemUser(@Param("userId") Long userId);

    /**
     * Find matches for a specific user (through found items)
     */
    @Query("SELECT m FROM Match m JOIN m.foundItem fi WHERE fi.user.id = :userId")
    List<Match> findMatchesForFoundItemUser(@Param("userId") Long userId);
}
