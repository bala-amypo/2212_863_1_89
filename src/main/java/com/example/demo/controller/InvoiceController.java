package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

   
    @PostMapping("/upload/{userId}/{vendorId}")
    public ResponseEntity<Invoice> uploadInvoice(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @RequestBody Invoice invoice) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + userId));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID " + vendorId));

        invoice.setUser(user);
        invoice.setVendor(vendor);

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    @PostMapping("/categorize/{invoiceId}")
    public ResponseEntity<Invoice> categorizeInvoice(@PathVariable Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID " + invoiceId));

        Category category;
        if (invoice.getAmount() != null && invoice.getAmount() > 10000) {
            category = categoryRepository.findByName("High Value")
                    .orElseThrow(() -> new ResourceNotFoundException("Category 'High Value' not found"));
        } else {
            category = categoryRepository.findByName("Regular")
                    .orElseThrow(() -> new ResourceNotFoundException("Category 'Regular' not found"));
        }

        invoice.setCategory(category);
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

   
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getUserInvoices(@PathVariable Long userId) {
        
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + userId));

        return ResponseEntity.ok(invoiceRepository.findByUserId(userId));
    }

    
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID " + invoiceId));

        return ResponseEntity.ok(invoice);
    }
}
