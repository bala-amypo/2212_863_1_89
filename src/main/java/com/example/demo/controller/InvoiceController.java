package com.example.demo.controller;

import com.example.demo.entity.*;
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

    // 1️⃣ Upload Invoice
    @PostMapping("/upload/{userId}/{vendorId}")
    public ResponseEntity<Invoice> uploadInvoice(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @RequestBody Invoice invoice) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        invoice.setUser(user);
        invoice.setVendor(vendor);

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // 2️⃣ Categorize Invoice
    @PostMapping("/categorize/{invoiceId}")
    public ResponseEntity<Invoice> categorizeInvoice(@PathVariable Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Category category;
        if (invoice.getAmount() != null && invoice.getAmount() > 10000) {
            category = categoryRepository.findByName("High Value")
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            category = categoryRepository.findByName("Regular")
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        invoice.setCategory(category);
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // 3️⃣ Get invoices by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getUserInvoices(@PathVariable Long userId) {
        return ResponseEntity.ok(invoiceRepository.findByUserId(userId));
    }

    // 4️⃣ Get invoice by ID
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(
                invoiceRepository.findById(invoiceId)
                        .orElseThrow(() -> new RuntimeException("Invoice not found"))
        );
    }
}
