package com.example.demo.controller;

import com.example.demo.entity.Invoice;
import com.example.demo.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // POST /api/invoices/upload/{userId}/{vendorId}
    @PostMapping("/upload/{userId}/{vendorId}")
    public Invoice uploadInvoice(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @RequestBody Invoice invoice) {

        return invoiceService.uploadInvoice(userId, vendorId, invoice);
    }

    // POST /api/invoices/categorize/{invoiceId}
    @PostMapping("/categorize/{invoiceId}")
    public Invoice categorizeInvoice(@PathVariable Long invoiceId) {
        return invoiceService.categorizeInvoice(invoiceId);
    }

    // GET /api/invoices/user/{userId}
    @GetMapping("/user/{userId}")
    public List<Invoice> getInvoicesByUser(@PathVariable Long userId) {
        return invoiceService.getInvoicesByUser(userId);
    }

    // GET /api/invoices/{invoiceId}
    @GetMapping("/{invoiceId}")
    public Invoice getInvoiceById(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }
}
