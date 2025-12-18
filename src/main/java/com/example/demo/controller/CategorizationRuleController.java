package com.example.demo.controller;

import com.example.demo.model.CategorizationRule;
import com.example.demo.service.CategorizationRuleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class CategorizationRuleController {

    private final CategorizationRuleService ruleService;

    public CategorizationRuleController(
            CategorizationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public CategorizationRule createRule(
            @RequestBody CategorizationRule rule) {
        return ruleService.createRule(rule);
    }

    @GetMapping
    public List<CategorizationRule> getAllRules() {
        return ruleService.getAllRules();
    }
}
