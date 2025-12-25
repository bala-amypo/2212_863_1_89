package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices", uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "invoice_number"}))
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_number")
    private String invoiceNumber;
    
    private Double amount;
    private String description;
    
    @Column(name = "invoice_date")
    private LocalDate invoiceDate;
    
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;
    
    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
}