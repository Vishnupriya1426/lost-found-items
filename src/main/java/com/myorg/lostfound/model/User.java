package com.myorg.lostfound.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User entity representing users in the Lost & Found system
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // One-to-many relationship with LostItems
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LostItem> lostItems;

    // One-to-many relationship with FoundItems
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoundItem> foundItems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public User() {}

    public User(Long id, String email, String name, String phone, LocalDateTime createdAt, List<LostItem> lostItems, List<FoundItem> foundItems) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
        this.lostItems = lostItems;
        this.foundItems = foundItems;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<LostItem> getLostItems() {
        return lostItems;
    }

    public void setLostItems(List<LostItem> lostItems) {
        this.lostItems = lostItems;
    }

    public List<FoundItem> getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(List<FoundItem> foundItems) {
        this.foundItems = foundItems;
    }
}
