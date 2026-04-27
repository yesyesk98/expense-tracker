package com.example.expense_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.expense_tracker.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
}