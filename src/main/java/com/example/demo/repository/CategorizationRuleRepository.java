package com.example.demo.repository;

import com.example.demo.entity.CategorizationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategorizationRuleRepository extends JpaRepository<CategorizationRule, Long> {

    @Query("""
        SELECT r FROM CategorizationRule r
        WHERE LOWER(:description) LIKE LOWER(CONCAT('%', r.keyword, '%'))
        ORDER BY r.priority DESC
    """)
    List<CategorizationRule> findMatchingRulesByDescription(String description);
}
