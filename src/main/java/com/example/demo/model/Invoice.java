package com.example.demo.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"invoiceNumber", "vendor_id"})
})
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    private String invoiceNumber;
    private Double amount;
    private LocalDate invoiceDate;
    private String description;

    @ManyToOne
    private Category category; // initially null

    @ManyToOne
    private User uploadedBy;

    private LocalDateTime uploadedAt;

    @PrePersist
    void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    
}
