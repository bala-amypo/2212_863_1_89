package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.util.InvoiceCategorizationEngine;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final CategorizationRuleRepository ruleRepository;
    private final InvoiceCategorizationEngine engine;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            UserRepository userRepository,
            CategorizationRuleRepository ruleRepository,
            InvoiceCategorizationEngine engine) {

        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.ruleRepository = ruleRepository;
        this.engine = engine;
    }

    @Override
    public Invoice uploadInvoice(Invoice invoice) {
        List<CategorizationRule> rules =
                ruleRepository.findMatchingRulesByDescription(
                        invoice.getDescription());

        Category category = engine.determineCategory(invoice, rules);
        invoice.setCategory(category);

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invoice not found"));
    }

    @Override
    public List<Invoice> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        return invoiceRepository.findByUploadedBy(user);
    }
}
