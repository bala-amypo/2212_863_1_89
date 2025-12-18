package com.example.demo.util;

import com.example.demo.model.Category;
import com.example.demo.model.CategorizationRule;
import com.example.demo.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceCategorizationEngine {

    public Category determineCategory(
            Invoice invoice,
            List<CategorizationRule> rules) {

        rules.sort((r1, r2) -> r2.getPriority() - r1.getPriority());

        for (CategorizationRule rule : rules) {
            String description = invoice.getDescription();
            String keyword = rule.getKeyword();

            if (description == null || keyword == null) {
                continue;
            }

            switch (rule.getMatchType()) {
                case "EXACT":
                    if (description.equals(keyword)) {
                        return rule.getCategory();
                    }
                    break;

                case "CONTAINS":
                    if (description.contains(keyword)) {
                        return rule.getCategory();
                    }
                    break;

                case "REGEX":
                    if (description.matches(keyword)) {
                        return rule.getCategory();
                    }
                    break;
            }
        }
        return null;
    }
}
