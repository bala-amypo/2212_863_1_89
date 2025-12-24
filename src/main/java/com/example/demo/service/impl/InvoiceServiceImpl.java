package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CategorizationRule;
import com.example.demo.model.Category;
import com.example.demo.model.Invoice;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
import com.example.demo.repository.CategorizationRuleRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VendorRepository;
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
    private final InvoiceCategorizationEngine categorizationEngine;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                             UserRepository userRepository,
                             VendorRepository vendorRepository,
                             CategorizationRuleRepository ruleRepository,
                             InvoiceCategorizationEngine categorizationEngine) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.ruleRepository = ruleRepository;
        this.categorizationEngine = categorizationEngine;
    }

    @Override
    public Invoice uploadInvoice(Long userId, Long vendorId, Invoice invoice) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        if (invoice.getAmount() == null || invoice.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        invoice.setUploadedBy(user);
        invoice.setVendor(vendor);
        invoice.setCategory(null);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice categorizeInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        List<CategorizationRule> rules = ruleRepository.findMatchingRulesByDescription(
                invoice.getDescription() != null ? invoice.getDescription() : ""
        );

        Category category = categorizationEngine.determineCategory(invoice, rules);
        invoice.setCategory(category);

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return invoiceRepository.findByUploadedBy(user);
    }

    @Override
    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }
}