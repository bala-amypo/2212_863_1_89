package com.example.demo.service.impl;

import com.example.demo.entity.CategorizationRule;
import com.example.demo.entity.Category;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategorizationRuleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategorizationRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategorizationRuleServiceImpl implements CategorizationRuleService {

    private final CategorizationRuleRepository ruleRepository;
    private final CategoryRepository categoryRepository;

    public CategorizationRuleServiceImpl(
            CategorizationRuleRepository ruleRepository,
            CategoryRepository categoryRepository) {
        this.ruleRepository = ruleRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategorizationRule createRule(Long categoryId, CategorizationRule rule) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        rule.setCategory(category);
        return ruleRepository.save(rule);
    }

    public List<CategorizationRule> getRulesByCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return ruleRepository.findAll();
    }

    public void deleteRule(Long ruleId) {
        if (!ruleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("Rule not found");
        }
        ruleRepository.deleteById(ruleId);
    }
}
