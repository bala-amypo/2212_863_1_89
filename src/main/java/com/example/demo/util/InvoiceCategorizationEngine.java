package com.example.demo.util;

import com.example.demo.model.*;
import java.util.List;

public class InvoiceCategorizationEngine {

    public Category determineCategory(Invoice invoice, List<CategorizationRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return null;
        }

        String description = invoice.getDescription();
        if (description == null) return null;

        for (CategorizationRule rule : rules) {
            String keyword = rule.getKeyword();

            switch (rule.getMatchType()) {
                case "EXACT":
                    if (description.equals(keyword)) return rule.getCategory();
                    break;

                case "CONTAINS":
                    if (description.toLowerCase().contains(keyword.toLowerCase()))
                        return rule.getCategory();
                    break;

                case "REGEX":
                    if (description.matches(keyword)) return rule.getCategory();
                    break;
            }
        }
        return null;
    }
}
