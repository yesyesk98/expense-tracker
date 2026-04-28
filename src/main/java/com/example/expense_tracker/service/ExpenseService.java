package com.example.expense_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org,springframework.web.ResponseStatusException;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Expense> getAllExpenses() {

        return expenseRepository.findAll();
    }

    public Expense updateExpensePartially(Long id, Expense expense) {
        Optional<Expense> result = ExpexpenseRepository.findById(id);
        // Great learning to implement lambda here
        if (result.isPresent()) {

        } else {

        }

        return expenseRepository.save(expense);
    }

}
