package com.example.demo.service;

import com.example.demo.model.CategorizationRule;

import java.util.List;

public interface CategorizationRuleService {

    List<CategorizationRule> getAllRules();
    CategorizationRule saveRule(CategorizationRule rule);
    List<CategorizationRule> findRulesByDescription(String description);

}
