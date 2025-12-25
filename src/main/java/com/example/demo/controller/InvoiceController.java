package com.example.demo.controller;

import com.example.demo.model.Invoice;
import com.example.demo.service.impl.InvoiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {
    
    @Autowired
    private InvoiceServiceImpl invoiceService;
    
    @PostMapping("/upload")
    public ResponseEntity<Invoice> uploadInvoice(
            @RequestParam Long userId,
            @RequestParam Long vendorId,
            @RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.uploadInvoice(userId, vendorId, invoice);
        return ResponseEntity.ok(savedInvoice);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        return ResponseEntity.ok(invoice);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUser(@PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByUser(userId);
        return ResponseEntity.ok(invoices);
    }
}