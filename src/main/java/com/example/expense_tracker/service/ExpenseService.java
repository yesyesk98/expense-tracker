package com.example.expense_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Optional<Expense> existing = expenseRepository.findById(id);
        // Great learning to implement lambda here
        if (existing.isPresent()) 
        {
            existing.get().setDescription(expense.getDescription());
            existing.get().setAmount(expense.getAmount());
            existing.get().setCategory(expense.getCategory());
            existing.get().setExpenseDate(expense.getExpenseDate());
            existing.get().setNotes(expense.getNotes());
            existing.get().setUpdatedAt(LocalDateTime.now());
            return expenseRepository.save(existing.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + id);
            //Your custom exception; only useful if you have a global handler (@ControllerAdvice) mapping it to 404.
            //throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
    }

}
