package com.example.expensetracker.service;

import com.example.expensetracker.dto.TransactionForm;
import com.example.expensetracker.entity.Transaction;
import com.example.expensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import com.example.expensetracker.repository.AccountRepository;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.entity.Account;
import com.example.expensetracker.entity.Category;

import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + id));
    }

    public Transaction create(TransactionForm form) {
        Account account = accountRepository.findById(form.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + form.getAccountId()));
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + form.getCategoryId()));
        Transaction transaction = Transaction.builder()
                .amount(form.getAmount())
                .occurredOn(form.getOccurredOn())
                .note(form.getNote())
                .account(account)
                .category(category)
                .build();
        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, TransactionForm form) {
        Transaction transaction = findById(id);
        Account account = accountRepository.findById(form.getAccountId())
                        .orElseThrow(() -> new EntityNotFoundException("Account not found: " + form.getAccountId()));
        Category category = categoryRepository.findById(form.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found: " + form.getCategoryId()));
        transaction.setAmount(form.getAmount());
        transaction.setOccurredOn(form.getOccurredOn());
        transaction.setNote(form.getNote());
        transaction.setAccount(account);
        transaction.setCategory(category);
        return transactionRepository.save(transaction);
    }

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Transaction not found: " + id);
        }
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findFiltered(LocalDate start, LocalDate end, Long categoryId) {
        if (start == null) {
            start = LocalDate.now().withDayOfMonth(1);
        }
        if (end == null) {
            end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }
        if (categoryId == null) {
            return transactionRepository.findByOccurredOnBetweenOrderByOccurredOnDesc(start, end);
        }

        return transactionRepository.findByOccurredOnBetweenAndCategory_IdOrderByOccurredOnDesc(start, end, categoryId);
    }

}

// findById(Long),
// create(TransactionForm),
// update(Long, TransactionForm),
// delete(Long)
//findFiltered(LocalDate start, LocalDate end, Long categoryId)
// — if categoryId is null, use the date-only query;
// otherwise the date+category one.
// Defaults: if start/end are null, default to the current month.
//create and update must look up the Account and Category from their IDs
// in the form and attach the entity references.
// Throw EntityNotFoundException if either is missing.