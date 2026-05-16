package com.example.expensetracker.controller;

import com.example.expensetracker.dto.TransactionForm;
import com.example.expensetracker.entity.Transaction;
import com.example.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

import com.example.expensetracker.service.AccountService;
import com.example.expensetracker.service.CategoryService;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final CategoryService categoryService;

    @GetMapping("/transactions")
    public String list(@RequestParam(required = false) LocalDate start,
                       @RequestParam(required = false) LocalDate end,
                       @RequestParam(required = false) Long categoryId,
                       Model model) {
        model.addAttribute("transactions", transactionService.findFiltered(start, end, categoryId));
        return "transactions/list";
    }

    @GetMapping("/transactions/new")
    public String newForm(Model model) {
        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "transactions/form";
    }

    @PostMapping("/transactions")
    public String create(@Valid @ModelAttribute("transactionForm") TransactionForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("mode", "create");
            return "transactions/form";
        }
        Transaction saved = transactionService.create(form);
        flash.addFlashAttribute("success", "Transaction created.");
        return "redirect:/transactions";
    }

    @GetMapping("/transactions/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.findById(id);
        TransactionForm form = new TransactionForm(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getOccurredOn(),
                transaction.getNote(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId()
        );
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("transactionForm", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("pageTitle", "Edit Transaction");
        return "transactions/form";

    }

    @PostMapping("/transactions/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("transactionForm")
                         TransactionForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accounts", accountService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("mode", "edit");
            return "transactions/form";
        }
        transactionService.update(id, form);
        flash.addFlashAttribute("success", "Transaction updated");
        return "redirect:/transactions";
    }

    @PostMapping("/transactions/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        transactionService.delete(id);
        flash.addFlashAttribute("success", "Transaction deleted.");
        return "redirect:/transactions";

    }
}


// list with filters (start, end, categoryId as query params)
// new form
// create
// edit
// update
// delete