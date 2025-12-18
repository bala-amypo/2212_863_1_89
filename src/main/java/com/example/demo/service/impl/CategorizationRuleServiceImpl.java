package com.example.demo.service;

import com.example.demo.model.CategorizationRule;
import com.example.demo.repository.CategorizationRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorizationRuleServiceImpl implements CategorizationRuleService {

    private final CategorizationRuleRepository repository;

    public CategorizationRuleServiceImpl(CategorizationRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategorizationRule> getAllRules() {
        return repository.findAll();
    }

    @Override
    public CategorizationRule saveRule(CategorizationRule rule) {
        return repository.save(rule);
    }

    @Override
    public List<CategorizationRule> findRulesByDescription(String description) {
        return repository.findMatchingRulesByDescription(description);
    }
}
