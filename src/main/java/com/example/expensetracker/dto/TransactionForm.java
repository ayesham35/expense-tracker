package com.example.expensetracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionForm {

    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate occurredOn;

    @Size(max = 255, message = "Note cannot exceed 255 characters")
    private String note;

    @NotNull(message = "Account is required")
    private Long accountId;

    @NotNull(message = "Category is required")
    private Long categoryId;

}

// TransactionForm:
// id,
// amount (NotNull, @DecimalMin(value="0.01")),
// occurredOn (NotNull, @PastOrPresent),
// note (≤255),
// accountId (NotNull, Long),
// categoryId (NotNull, Long)
