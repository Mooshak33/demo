package com.automation.demo.service;

import com.automation.demo.Exception.InsufficientFundsException;
import com.automation.demo.Exception.ResourceNotFoundException;
import com.automation.demo.model.BankAccount;
import com.automation.demo.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount createAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    public Optional<BankAccount> updateAccount(Long id, BankAccount accountDetails) {
        return bankAccountRepository.findById(id).map(account -> {
            account.setAccountHolderName(accountDetails.getAccountHolderName());
            account.setBalance(accountDetails.getBalance());
            return bankAccountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    public void deleteAccount(Long id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id " + id);
        }
        bankAccountRepository.deleteById(id);
    }

    public BankAccount getAccount(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    @Transactional
    public void transferFunds(Long fromAccountId, Long toAccountId, Double amount) {
        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + fromAccountId));
        BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + toAccountId));

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in account with id " + fromAccountId);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }
}
