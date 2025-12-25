package com.example.demo.service;

import com.example.demo.model.Vendor;
import java.util.List;

public interface VendorService {
    Vendor createVendor(Vendor vendor);
    List<Vendor> getAllVendors();
    Vendor getVendor(Long id);
}