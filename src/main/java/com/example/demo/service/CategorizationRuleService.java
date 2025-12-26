package com.example.demo.service;

import com.example.demo.model.CategorizationRule;
import java.util.List;

public interface CategorizationRuleService {
    CategorizationRule createRule(CategorizationRule rule);
  
    CategorizationRule getRule(Long id);

    void deleteRule(Long id);
}
