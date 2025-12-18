package com.example.demo.controller;

import com.example.demo.model.Invoice;
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

    @PostMapping
    public Invoice uploadInvoice(@RequestBody Invoice invoice) {
        return invoiceService.uploadInvoice(invoice);
    }

    @GetMapping("/{id}")
    public Invoice getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoice(id);
    }

    @GetMapping("/user/{userId}")
    public List<Invoice> getInvoicesByUser(
            @PathVariable Long userId) {
        return invoiceService.getInvoicesByUser(userId);
    }
}
