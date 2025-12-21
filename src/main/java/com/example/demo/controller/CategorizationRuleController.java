package com.example.demo.controller;

import com.example.demo.entity.CategorizationRule;
import com.example.demo.service.CategorizationRuleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class CategorizationRuleController {

    private final CategorizationRuleService ruleService;

    public CategorizationRuleController(CategorizationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/category/{categoryId}")
    public CategorizationRule create(
            @PathVariable Long categoryId,
            @RequestBody CategorizationRule rule) {
        return ruleService.createRule(categoryId, rule);
    }

    @GetMapping("/category/{categoryId}")
    public List<CategorizationRule> getByCategory(@PathVariable Long categoryId) {
        return ruleService.getRulesByCategory(categoryId);
    }

    @DeleteMapping("/{ruleId}")
    public void delete(@PathVariable Long ruleId) {
        ruleService.deleteRule(ruleId);
    }
}
