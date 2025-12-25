package com.example.demo.service.impl;

import com.example.demo.model.CategorizationRule;
import com.example.demo.repository.CategorizationRuleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategorizationRuleService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorizationRuleServiceImpl implements CategorizationRuleService {
    private final CategorizationRuleRepository ruleRepository;
    private final CategoryRepository categoryRepository;
    
    public CategorizationRuleServiceImpl(CategorizationRuleRepository ruleRepository, CategoryRepository categoryRepository) {
        this.ruleRepository = ruleRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public CategorizationRule createRule(CategorizationRule rule) {
        return ruleRepository.save(rule);
    }
    
    @Override
    public List<CategorizationRule> getAllRules() {
        return ruleRepository.findAll();
    }
    
    @Override
    public CategorizationRule getRule(Long id) {
        return ruleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
    }
    
    @Override
    public List<CategorizationRule> findMatchingRules(String description) {
        return ruleRepository.findMatchingRulesByDescription(description);
    }
}