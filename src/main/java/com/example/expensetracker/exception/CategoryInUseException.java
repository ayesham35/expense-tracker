package com.example.expensetracker.exception;

public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(Long id) {
        super("Category " + id + " cannot be deleted because it has existing transactions");
    }
}
