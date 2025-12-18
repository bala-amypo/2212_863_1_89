package com.example.demo.service;

import com.example.demo.model.CategorizationRule;
import com.example.demo.repository.CategorizationRuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorizationRuleServiceImpl
        implements CategorizationRuleService {

    private final CategorizationRuleRepository ruleRepository;

    public CategorizationRuleServiceImpl(
            CategorizationRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public CategorizationRule createRule(CategorizationRule rule) {
        return ruleRepository.save(rule);
    }

    @Override
    public List<CategorizationRule> getAllRules() {
        return ruleRepository.findAll();
    }
}
