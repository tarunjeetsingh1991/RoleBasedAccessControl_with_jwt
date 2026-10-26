package com.fdmgroup.springbootdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.springbootdemo.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByCustomerAddressCity(String city);
    
    List<Account> findByCustomerCustomerId(Long customerId);
    
    List<Account> findByBalance(Double balance);
    
    List<Account> findByCustomerCustomerNameIgnoreCase(String customerName);
}