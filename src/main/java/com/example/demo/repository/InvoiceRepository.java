package com.example.demo.repository;

import com.example.demo.entity.Invoice;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUploadedBy(User user);

    @Query("SELECT i FROM Invoice i WHERE i.amount > :amount")
    List<Invoice> findByAmountGreaterThanHql(Double amount);
}
