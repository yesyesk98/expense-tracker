package com.example.expense_tracker.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.service.ExpenseService;  

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    //(Next phase) switch from entity input to DTO input.
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense savedExpense = expenseService.createExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Expense> updateExpensePartially(@PathVariable Long id, @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpensePartially(id, expense);
        return ResponseEntity.ok(updatedExpense);
    }
}

