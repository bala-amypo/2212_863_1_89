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

    public Category determineCategory(
            Invoice invoice,
            List<CategorizationRule> rules) {

        if (rules == null || rules.isEmpty()) {
            return null;
        }

        return rules.stream()
                .sorted(Comparator.comparing(CategorizationRule::getPriority).reversed())
                .filter(rule -> matches(rule, invoice.getDescription()))
                .map(CategorizationRule::getCategory)
                .findFirst()
                .orElse(null);
    }

    private boolean matches(CategorizationRule rule, String description) {
        if (description == null) return false;

        switch (rule.getMatchType()) {
            case "EXACT":
                return description.equals(rule.getKeyword());

            case "CONTAINS":
                return description.toLowerCase()
                        .contains(rule.getKeyword().toLowerCase());

            case "REGEX":
                return Pattern.compile(rule.getKeyword())
                        .matcher(description)
                        .find();

            default:
                return false;
        }
    }
}
