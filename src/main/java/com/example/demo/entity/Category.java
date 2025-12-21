package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(columnNames = "categoryName")
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String categoryName;

    private String description;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    private Set<Invoice> invoices;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<CategorizationRule> rules;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
