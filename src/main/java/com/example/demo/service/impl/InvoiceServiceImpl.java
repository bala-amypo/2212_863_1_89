package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.InvoiceService;
import com.example.demo.util.InvoiceCategorizationEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final CategorizationRuleRepository ruleRepository;
    private final InvoiceCategorizationEngine engine;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            UserRepository userRepository,
            VendorRepository vendorRepository,
            CategorizationRuleRepository ruleRepository,
            InvoiceCategorizationEngine engine) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.ruleRepository = ruleRepository;
        this.engine = engine;
    }

    public Invoice uploadInvoice(Long userId, Long vendorId, Invoice invoice) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        invoice.setUploadedBy(user);
        invoice.setVendor(vendor);
        invoice.setCategory(null);
        return invoiceRepository.save(invoice);
    }

    public Invoice categorizeInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        List<CategorizationRule> rules =
                ruleRepository.findMatchingRulesByDescription(invoice.getDescription());

        Category category = engine.determineCategory(invoice, rules);
        invoice.setCategory(category);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return invoiceRepository.findByUploadedBy(user);
    }

    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }
}
