package com.example.expensetracker.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CategorySpendRow {

    private String categoryName;
    private BigDecimal total;

}

