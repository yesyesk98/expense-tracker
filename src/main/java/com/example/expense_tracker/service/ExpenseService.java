package com.example.expense_tracker.service;

import org.springframework.stereotype.Service;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;

import java.time.LocalDateTime;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    public Expense createExpense(Expense expense) {
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());
        return expenseRepository.save(expense);
    }
}
