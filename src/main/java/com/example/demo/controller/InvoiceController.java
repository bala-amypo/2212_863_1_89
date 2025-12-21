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

    @PostMapping("/upload/{userId}/{vendorId}")
    public Invoice upload(
            @PathVariable Long userId,
            @PathVariable Long vendorId,
            @RequestBody Invoice invoice) {
        return invoiceService.uploadInvoice(userId, vendorId, invoice);
    }

    @PostMapping("/categorize/{invoiceId}")
    public Invoice categorize(@PathVariable Long invoiceId) {
        return invoiceService.categorizeInvoice(invoiceId);
    }

    @GetMapping("/user/{userId}")
    public List<Invoice> getByUser(@PathVariable Long userId) {
        return invoiceService.getInvoicesByUser(userId);
    }

    @GetMapping("/{invoiceId}")
    public Invoice get(@PathVariable Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }
}
