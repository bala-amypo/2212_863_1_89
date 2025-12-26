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



    @GetMapping("/{id}")
    public ResponseEntity<CategorizationRule> getRule(@PathVariable Long id) {
        CategorizationRule rule = ruleService.getRule(id);
        return ResponseEntity.ok(rule);
    }

   

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        CategorizationRule rule = ruleService.getRule(id); // verify rule exists
        ruleService.deleteRule(id); // call delete
        return ResponseEntity.noContent().build();
    }
}
