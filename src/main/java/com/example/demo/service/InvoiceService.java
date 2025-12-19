package com.example.demo.service;

import com.example.demo.model.Invoice;
import java.util.List;

public interface InvoiceService {
    Invoice uploadInvoice(Invoice invoice);
    Invoice getInvoice(Long id);
    List<Invoice> getInvoicesByUser(Long userId);
}
