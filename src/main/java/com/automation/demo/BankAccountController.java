package com.automation.demo;

import com.automation.demo.model.BankAccount;
import com.automation.demo.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public BankAccount createAccount(@RequestBody BankAccount account) {
        return bankAccountService.createAccount(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long id, @RequestBody BankAccount accountDetails) {
        return bankAccountService.updateAccount(id, accountDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        bankAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable Long id) {
        BankAccount account = bankAccountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
        bankAccountService.transferFunds(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().build();
    }
}