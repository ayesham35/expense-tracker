package com.example.expensetracker.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import com.example.expensetracker.entity.Transaction;
import com.example.expensetracker.dto.CategorySpendRow;

@Getter
@AllArgsConstructor
public class DashboardData {

    private LocalDate start;
    private LocalDate end;
    private BigDecimal total;
    private List<CategorySpendRow> topCategories;
    private List<Transaction> recent;
}
