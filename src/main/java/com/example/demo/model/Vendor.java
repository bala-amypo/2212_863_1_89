package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "vendors",
    uniqueConstraints = @UniqueConstraint(columnNames = "vendorName")
)
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vendorName;

    private String contactEmail;
    private String address;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "favoriteVendors")
    private Set<User> users = new HashSet<>();

    public Vendor() {}

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    
    public Long getId() { return id; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Set<User> getUsers() { return users; }
}
