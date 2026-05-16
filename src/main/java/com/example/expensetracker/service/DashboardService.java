package com.example.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.expensetracker.repository.TransactionRepository;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.example.expensetracker.dto.CategorySpendRow;
import java.util.List;
import com.example.expensetracker.entity.Transaction;
import com.example.expensetracker.dto.DashboardData;
import org.springframework.data.domain.PageRequest;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardData currentMonth() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());
        BigDecimal total = transactionRepository.sumExpensesBetween(start, end);
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        List<CategorySpendRow> topCategories =
                transactionRepository.topExpenseCategoriesBetween(start, end, PageRequest.of(0, 5));
        List<Transaction> recent = transactionRepository.findTop10ByOrderByOccurredOnDescIdDesc();

        return new DashboardData(start, end, total, topCategories, recent);

    }
}
