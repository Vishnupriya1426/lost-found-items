package com.myorg.lostfound.repository;

import com.myorg.lostfound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find users by name containing the given string (case insensitive)
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Find users by phone number
     */
    Optional<User> findByPhone(String phone);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Find users created after a specific date
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    List<User> findUsersCreatedAfter(@Param("date") java.time.LocalDateTime date);

    /**
     * Count total number of users
     */
    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();
}
