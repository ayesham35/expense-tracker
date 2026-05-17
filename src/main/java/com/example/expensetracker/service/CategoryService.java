package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryForm;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import com.example.expensetracker.entity.CategoryType;
import com.example.expensetracker.repository.TransactionRepository;
import com.example.expensetracker.exception.CategoryInUseException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Category> findByType(CategoryType type) {
        return categoryRepository.findByType(type);

    }

    public Category create(CategoryForm form) {
        Category category = Category.builder()
                .name(form.getName())
                .type(form.getType())
                .build();
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryForm form) {
        Category category = findById(id);
        category.setName(form.getName());
        category.setType(form.getType());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: " + id);
        }
        if (transactionRepository.countByCategory_Id(id) > 0) {
            throw new CategoryInUseException(id);
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> getTransactionCounts(List<Category> categories) {
        Map<Long, Long> counts = new HashMap<>();
        for (Category category : categories) {
            counts.put(category.getId(), transactionRepository.countByCategory_Id(category.getId()));
        }
        return counts;
    }

}


// findAll(),
// findById(Long),
// findByType(CategoryType),
// create(CategoryForm),
// update(Long, CategoryForm)
//delete(Long) — must check transactionRepository.countByCategory_Id(id) > 0
// and throw if so. Use a custom exception
// (CategoryInUseException extends RuntimeException)
// or IllegalStateException with a clear message.