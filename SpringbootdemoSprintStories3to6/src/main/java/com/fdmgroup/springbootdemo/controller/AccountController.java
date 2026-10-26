package com.fdmgroup.springbootdemo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fdmgroup.springbootdemo.dto.AccountResponseDto;
import com.fdmgroup.springbootdemo.model.Account;
import com.fdmgroup.springbootdemo.model.CheckingAccount;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/savings")
    public ResponseEntity<Account> createSavingsAccount(@Valid @RequestBody SavingsAccount account) {

        Long customerId = account.getCustomer().getCustomerId();

        Account created = service.createSavingsAccount(
                customerId,
                account.getBalance(),
                account.getInterestRate()
        );

        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/checking")
    public ResponseEntity<Account> createCheckingAccount(@Valid @RequestBody CheckingAccount account) {

        Long customerId = account.getCustomer().getCustomerId();

        Account created = service.createCheckingAccount(
                customerId,
                account.getBalance(),
                account.getNextCheckNumber()
        );

        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/savings/{accountId}")
    public ResponseEntity<SavingsAccount> updateSavingsAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody SavingsAccount account) {

        SavingsAccount updated = service.updateSavingsAccount(
                accountId,
                account.getBalance(),
                account.getInterestRate()
        );

        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/checking/{accountId}")
    public ResponseEntity<CheckingAccount> updateCheckingAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody CheckingAccount account) {

        CheckingAccount updated = service.updateCheckingAccount(
                accountId,
                account.getBalance(),
                account.getNextCheckNumber()
        );

        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/savings/{accountId}")
    public ResponseEntity<Void> deleteSavingsAccount(@PathVariable Long accountId) {
        service.deleteSavingsAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/checking/{accountId}")
    public ResponseEntity<Void> deleteCheckingAccount(@PathVariable Long accountId) {
        service.deleteCheckingAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/city/{city}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByCity(@PathVariable String city) {

        List<Account> accounts = service.getAccountsByCity(city);

        List<AccountResponseDto> response = accounts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {

        List<Account> accounts = service.getAllAccounts();

        List<AccountResponseDto> response = accounts.stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByCustomerId(
            @PathVariable Long customerId) {

        List<Account> accounts = service.getAccountsByCustomerId(customerId);

        List<AccountResponseDto> response = accounts.stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountById(
            @PathVariable Long accountId) {

        Account account = service.getAccountById(accountId);

        AccountResponseDto dto = mapToDto(account);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/balance/{balance}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByBalance(
            @PathVariable Double balance) {

        List<Account> accounts = service.getAccountsByBalance(balance);

        List<AccountResponseDto> response = accounts.stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    private AccountResponseDto mapToDto(Account account) {

        AccountResponseDto dto = new AccountResponseDto();

        dto.setAccountId(account.getAccountId());
        dto.setBalance(account.getBalance());

        if (account instanceof SavingsAccount savingsAccount) {
            dto.setAccountType("SAVINGS");
            dto.setInterestRate(savingsAccount.getInterestRate());
        } else if (account instanceof CheckingAccount checkingAccount) {
            dto.setAccountType("CHECKING");
            dto.setNextCheckNumber(checkingAccount.getNextCheckNumber());
        }

        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getCustomerId());
            dto.setCustomerName(account.getCustomer().getCustomerName());

            if (account.getCustomer().getAddress() != null) {
                dto.setCity(account.getCustomer().getAddress().getCity());
                dto.setProvince(account.getCustomer().getAddress().getProvince());
                dto.setPostalCode(account.getCustomer().getAddress().getPostalCode());
                dto.setStreetNumber(account.getCustomer().getAddress().getStreetNumber());
            }
        }

        return dto;
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/customer-name/{customerName}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByCustomerName(
            @PathVariable String customerName) {

        List<Account> accounts = service.getAccountsByCustomerName(customerName);

        List<AccountResponseDto> response = accounts.stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(response);
    }
}