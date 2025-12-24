package com.example.demo.controller;

import com.example.demo.model.CategorizationRule;
import com.example.demo.service.CategorizationRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@Tag(name = "Categorization Rules Endpoints")
public class CategorizationRuleController {

    private final CategorizationRuleService ruleService;

    public CategorizationRuleController(CategorizationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<CategorizationRule> createRule(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategorizationRule rule) {
        CategorizationRule created = ruleService.createRule(categoryId, rule);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategorizationRule>> getRulesByCategory(
            @PathVariable Long categoryId) {
        List<CategorizationRule> rules = ruleService.getRulesByCategory(categoryId);
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        ruleService.deleteRule(ruleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}