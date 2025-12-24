package com.example.demo.controller;

import com.example.demo.model.Invoice;
import com.example.demo.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoices Endpoints")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/upload/{userId}/{vendorId}")
    public ResponseEntity<Invoice> uploadInvoice(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @Valid @RequestBody Invoice invoice) {
        Invoice uploaded = invoiceService.uploadInvoice(userId, vendorId, invoice);
        return ResponseEntity.ok(uploaded);
    }

    @PostMapping("/categorize/{invoiceId}")
    public ResponseEntity<Invoice> categorizeInvoice(@PathVariable Long invoiceId) {
        Invoice categorized = invoiceService.categorizeInvoice(invoiceId);
        return ResponseEntity.ok(categorized);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUser(@PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByUser(userId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long invoiceId) {
        Invoice invoice = invoiceService.getInvoice(invoiceId);
        return ResponseEntity.ok(invoice);
    }
}