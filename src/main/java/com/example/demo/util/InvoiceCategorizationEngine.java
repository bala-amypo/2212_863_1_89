package com.example.demo.util;

import com.example.demo.model.Category;
import com.example.demo.model.CategorizationRule;
import com.example.demo.model.Invoice;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class InvoiceCategorizationEngine {
    
    public Category determineCategory(Invoice invoice, List<CategorizationRule> rules) {
        if (rules.isEmpty()) {
            return null;
        }
        
        for (CategorizationRule rule : rules) {
            if (matchesRule(invoice.getDescription(), rule)) {
                return rule.getCategory();
            }
        }
        
        return null;
    }
    
    private boolean matchesRule(String description, CategorizationRule rule) {
        if (description == null || rule.getKeyword() == null) {
            return false;
        }
        
        String matchType = rule.getMatchType();
        if ("EXACT".equals(matchType)) {
            return description.equals(rule.getKeyword());
        } else if ("CONTAINS".equals(matchType)) {
            return description.toLowerCase().contains(rule.getKeyword().toLowerCase());
        } else if ("REGEX".equals(matchType)) {
            return Pattern.matches(rule.getKeyword(), description);
        }
        
        return false;
    }
}