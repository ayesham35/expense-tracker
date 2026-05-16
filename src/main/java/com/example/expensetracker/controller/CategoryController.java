package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategoryForm;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.expensetracker.exception.CategoryInUseException;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/categories/new")
    public String newForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/form";
    }

    @PostMapping("/categories")
    public String create(@Valid @ModelAttribute("categoryForm") CategoryForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "categories/form";
        }
        Category saved = categoryService.create(form);
        flash.addFlashAttribute("success", "Category \"" + saved.getName() + "\" created.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        CategoryForm form = new CategoryForm(
                category.getId(),
                category.getName(),
                category.getType()
        );
        model.addAttribute("categoryForm", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("pageTitle", "Edit Category");
        return "categories/form";
    }

    @PostMapping("/categories/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("categoryForm") CategoryForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "categories/form";
        }
        categoryService.update(id, form);
        flash.addFlashAttribute("success", "Category updated.");
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            categoryService.delete(id);
            flash.addFlashAttribute("success", "Category deleted.");
        }
        catch (CategoryInUseException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categories";
    }


}

//list
//new form
//create
//edit
//update
//delete

