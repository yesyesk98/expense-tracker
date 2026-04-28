package com.example.expense_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public Expense updateExpensePartially(Long id, Expense expensePatch) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + id));

        if (expensePatch.getDescription() != null) {
            existingExpense.setDescription(expensePatch.getDescription());
        }
        if (expensePatch.getAmount() != null) {
            existingExpense.setAmount(expensePatch.getAmount());
        }
        if (expensePatch.getCategory() != null) {
            existingExpense.setCategory(expensePatch.getCategory());
        }
        if (expensePatch.getExpenseDate() != null) {
            existingExpense.setExpenseDate(expensePatch.getExpenseDate());
        }
        if (expensePatch.getNotes() != null) {
            existingExpense.setNotes(expensePatch.getNotes());
        }

        existingExpense.setUpdatedAt(LocalDateTime.now());
        return expenseRepository.save(existingExpense);
    }
}
