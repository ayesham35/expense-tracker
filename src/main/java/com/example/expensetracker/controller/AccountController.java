package com.example.expensetracker.controller;

import com.example.expensetracker.dto.AccountForm;
import com.example.expensetracker.entity.Account;
import com.example.expensetracker.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts")
    public String list(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "accounts/list";
    }

    @GetMapping("/accounts/new")
    public String newForm(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "accounts/form";
    }

    @PostMapping("/accounts")
    public String create(@Valid @ModelAttribute("accountForm") AccountForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "accounts/form";
        }
        Account saved = accountService.create(form);
        flash.addFlashAttribute("success", "Account \"" + saved.getName() + "\" created.");
        return "redirect:/accounts";

    }

    @GetMapping("/accounts/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Account account = accountService.findById(id);
        AccountForm form = new AccountForm(
                account.getId(),
                account.getName(),
                account.getType(),
                account.getOpeningBalance()
        );
        model.addAttribute("accountForm", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("pageTitle", "Edit Account");
        return "accounts/form";
    }

    @PostMapping("/accounts/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("accountForm") AccountForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "accounts/form";
        }
        accountService.update(id, form);
        flash.addFlashAttribute("success", "Account updated.");
        return "redirect:/accounts";
    }

    @PostMapping("/accounts/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        accountService.delete(id);
        flash.addFlashAttribute("success", "Account deleted.");
        return "redirect:/accounts";
    }

}
