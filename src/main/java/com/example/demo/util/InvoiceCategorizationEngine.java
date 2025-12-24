package com.example.demo.util;

import com.example.demo.model.Category;
import com.example.demo.model.CategorizationRule;
import com.example.demo.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class InvoiceCategorizationEngine {

    public Category determineCategory(Invoice invoice, List<CategorizationRule> rules) {
        if (invoice == null || rules == null || rules.isEmpty()) {
            return null;
        }

        String description = invoice.getDescription();
        if (description == null) {
            description = "";
        }

        // Sort rules by priority in descending order (highest priority first)
        List<CategorizationRule> sortedRules = rules.stream()
                .sorted(Comparator.comparing(CategorizationRule::getPriority).reversed())
                .toList();

        for (CategorizationRule rule : sortedRules) {
            if (matchesRule(description, rule)) {
                return rule.getCategory();
            }
        }

        return null;
    }

    private boolean matchesRule(String description, CategorizationRule rule) {
        String keyword = rule.getKeyword();
        String matchType = rule.getMatchType();

        if (keyword == null || matchType == null) {
            return false;
        }

        switch (matchType.toUpperCase()) {
            case "EXACT":
                return description.equalsIgnoreCase(keyword);
            
            case "CONTAINS":
                return description.toLowerCase().contains(keyword.toLowerCase());
            
            case "REGEX":
                try {
                    Pattern pattern = Pattern.compile(keyword);
                    return pattern.matcher(description).find();
                } catch (Exception e) {
                    return false;
                }
            
            default:
                return false;
        }
    }
}