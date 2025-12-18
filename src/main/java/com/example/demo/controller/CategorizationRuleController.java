package com.example.demo.controller;

import com.example.demo.model.CategorizationRule;
import com.example.demo.service.CategorizationRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class CategorizationRuleController {

    private final CategorizationRuleService service;

    public CategorizationRuleController(CategorizationRuleService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategorizationRule> getAllRules() {
        return service.getAllRules();
    }

    @PostMapping
    public CategorizationRule createRule(@RequestBody CategorizationRule rule) {
        return service.saveRule(rule);
    }

    @GetMapping("/search")
    public List<CategorizationRule> searchByDescription(@RequestParam String description) {
        return service.findRulesByDescription(description);
    }
}
