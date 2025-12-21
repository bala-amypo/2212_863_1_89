package com.example.demo.service;

import com.example.demo.entity.Invoice;
import java.util.List;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoice);
    List<Invoice> getAllInvoices();
}
