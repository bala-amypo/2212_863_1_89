package com.example.demo.repository;

import com.example.demo.model.CategorizationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategorizationRuleRepository extends JpaRepository<CategorizationRule, Long> {
    @Query("SELECT r FROM CategorizationRule r WHERE " +
           "LOWER(r.keyword) LIKE LOWER(CONCAT('%', :description, '%')) " +
           "OR LOWER(:description) LIKE LOWER(CONCAT('%', r.keyword, '%')) " +
           "ORDER BY r.priority DESC")
    List<CategorizationRule> findMatchingRulesByDescription(@Param("description") String description);
}