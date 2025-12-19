package com.example.demo.repository;

import com.example.demo.model.CategorizationRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategorizationRuleRepository extends JpaRepository<CategorizationRule, Long> {

    @Query("""
        SELECT r FROM CategorizationRule r
        WHERE 
        (r.matchType = 'EXACT' AND r.keyword = :description)
        OR
        (r.matchType = 'CONTAINS' AND LOWER(:description) LIKE LOWER(CONCAT('%', r.keyword, '%')))
        OR
        (r.matchType = 'REGEX' AND :description REGEXP r.keyword)
        ORDER BY r.priority DESC
    """)
    List<CategorizationRule> findMatchingRulesByDescription(@Param("description") String description);
}
