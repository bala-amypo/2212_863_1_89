package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.CategorizationRule;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CategorizationRuleRepository;
import com.example.demo.service.CategorizationRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class CategorizationRuleServiceImpl implements CategorizationRuleService {

    private final CategorizationRuleRepository ruleRepository;
    private final CategoryRepository categoryRepository;

    public CategorizationRuleServiceImpl(CategorizationRuleRepository ruleRepository,
                                        CategoryRepository categoryRepository) {
        this.ruleRepository = ruleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategorizationRule createRule(Long categoryId, CategorizationRule rule) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (rule.getKeyword() == null || rule.getKeyword().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }

        if (rule.getMatchType() == null || 
            (!rule.getMatchType().equals("EXACT") && 
             !rule.getMatchType().equals("CONTAINS") && 
             !rule.getMatchType().equals("REGEX"))) {
            throw new IllegalArgumentException("Match type must be EXACT, CONTAINS, or REGEX");
        }

        if (rule.getPriority() == null || rule.getPriority() <= 0) {
            throw new IllegalArgumentException("Priority must be greater than 0");
        }

        rule.setCategory(category);
        return ruleRepository.save(rule);
    }

    @Override
    public List<CategorizationRule> getRulesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return ruleRepository.findAll().stream()
                .filter(rule -> rule.getCategory().getId().equals(categoryId))
                .sorted(Comparator.comparing(CategorizationRule::getPriority).reversed())
                .toList();
    }

    @Override
    public void deleteRule(Long ruleId) {
        if (!ruleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("Rule not found");
        }
        ruleRepository.deleteById(ruleId);
    }
}