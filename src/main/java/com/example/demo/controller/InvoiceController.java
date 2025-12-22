package com.example.demo.controller;

import com.example.demo.entity.Invoice;
import com.example.demo.entity.User;
import com.example.demo.entity.Vendor;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;
    
    @PostMapping("/upload/{userId}/{vendorId}")
    public ResponseEntity<Invoice> uploadInvoice(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @RequestBody Invoice invoiceRequest) {

        // Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch Vendor
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));

        // Set associations
        invoiceRequest.setVendor(vendor);

        // Optional: You can set user if Invoice has a user field
        // invoiceRequest.setUser(user);

        // Save Invoice
        Invoice savedInvoice = invoiceRepository.save(invoiceRequest);

        return ResponseEntity.ok(savedInvoice);
    }

    // Optional: Get all invoices
    @GetMapping
    public ResponseEntity<Iterable<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }
}
