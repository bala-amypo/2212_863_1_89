package com.example.demo.controller;

import com.example.demo.model.CategorizationRule;
import com.example.demo.service.impl.CategorizationRuleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin(origins = "*")
public class CategorizationRuleController {
    
    @Autowired
    private CategorizationRuleServiceImpl ruleService;
    
    @PostMapping
    public ResponseEntity<CategorizationRule> createRule(@RequestBody CategorizationRule rule) {
        CategorizationRule savedRule = ruleService.createRule(rule);
        return ResponseEntity.ok(savedRule);
    }
    
    @GetMapping
    public ResponseEntity<List<CategorizationRule>> getAllRules() {
        List<CategorizationRule> rules = ruleService.getAllRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategorizationRule> getRule(@PathVariable Long id) {
        CategorizationRule rule = ruleService.getRule(id);
        return ResponseEntity.ok(rule);
    }
    
    @GetMapping("/match")
    public ResponseEntity<List<CategorizationRule>> findMatchingRules(@RequestParam String description) {
        List<CategorizationRule> rules = ruleService.findMatchingRules(description);
        return ResponseEntity.ok(rules);
    }
}