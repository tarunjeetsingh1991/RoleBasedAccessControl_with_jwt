package com.fdmgroup.springbootdemo.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fdmgroup.springbootdemo.exception.ResourceNotFoundException;
import com.fdmgroup.springbootdemo.model.Account;
import com.fdmgroup.springbootdemo.model.CheckingAccount;
import com.fdmgroup.springbootdemo.model.Customer;
import com.fdmgroup.springbootdemo.model.SavingsAccount;
import com.fdmgroup.springbootdemo.repository.AccountRepository;
import com.fdmgroup.springbootdemo.repository.CustomerRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public Account createSavingsAccount(Long customerId,
                                        double balance,
                                        double interestRate) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        SavingsAccount account = new SavingsAccount();
        account.setBalance(balance);
        account.setInterestRate(interestRate);

        account.setCustomer(customer);
        customer.getAccounts().add(account);

        return accountRepository.save(account);
    }

    public Account createCheckingAccount(Long customerId,
                                         double balance,
                                         int nextCheckNumber) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        CheckingAccount account = new CheckingAccount();
        account.setBalance(balance);
        account.setNextCheckNumber(nextCheckNumber);

        account.setCustomer(customer);
        customer.getAccounts().add(account);

        return accountRepository.save(account);
    }

    
    public SavingsAccount updateSavingsAccount(Long accountId,
            double balance,
            double interestRate) {

	Account existingAccount = accountRepository.findById(accountId)
	.orElseThrow(() ->
	new ResourceNotFoundException("Account not found with id: " + accountId));
	
	if (!(existingAccount instanceof SavingsAccount savingsAccount)) {
	throw new IllegalArgumentException("Account with id " + accountId + " is not a savings account");
	}
	
	savingsAccount.setBalance(balance);
	savingsAccount.setInterestRate(interestRate);
	
	return accountRepository.save(savingsAccount);
	}
	
	public CheckingAccount updateCheckingAccount(Long accountId,
	              double balance,
	              int nextCheckNumber) {
	
	Account existingAccount = accountRepository.findById(accountId)
	.orElseThrow(() ->
	new ResourceNotFoundException("Account not found with id: " + accountId));
	
	if (!(existingAccount instanceof CheckingAccount checkingAccount)) {
	throw new IllegalArgumentException("Account with id " + accountId + " is not a checking account");
	}
	
	checkingAccount.setBalance(balance);
	checkingAccount.setNextCheckNumber(nextCheckNumber);
	
	return accountRepository.save(checkingAccount);
	}
	
	
    public void deleteSavingsAccount(Long accountId) {

        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with id: " + accountId));

        if (!(existingAccount instanceof SavingsAccount)) {
            throw new IllegalArgumentException("Account with id " + accountId + " is not a savings account");
        }

        accountRepository.delete(existingAccount);
    }
    
    
    public void deleteCheckingAccount(Long accountId) {

        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with id: " + accountId));

        if (!(existingAccount instanceof CheckingAccount)) {
            throw new IllegalArgumentException("Account with id " + accountId + " is not a checking account");
        }

        accountRepository.delete(existingAccount);
    }
    
    public List<Account> getAccountsByCity(String city) {
        return accountRepository.findByCustomerAddressCity(city);
    }
    
    @Cacheable(value = "ACCOUNT")
    public List<Account> getAllAccounts() {
    	System.out.println("DB hit......");
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with id: " + customerId);
        }

        return accountRepository.findByCustomerCustomerId(customerId);
    }

    public Account getAccountById(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId));
    }
    
    
    public List<Account> getAccountsByBalance(Double balance) {
        return accountRepository.findByBalance(balance);
    }
    
    public List<Account> getAccountsByCustomerName(String customerName) {
        return accountRepository.findByCustomerCustomerNameIgnoreCase(customerName);
    }
}

