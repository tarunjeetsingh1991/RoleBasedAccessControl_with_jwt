package com.fdmgroup.springbootdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

import com.fdmgroup.springbootdemo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByAddressCity(String city);
    
    // if we want to use jpql, use this query : 
    /*
    @Query("""
            SELECT DISTINCT c
            FROM Customer c
            JOIN c.accounts a
            WHERE TYPE(a) = com.fdmgroup.springbootdemo.model.SavingsAccount
            """)
     List<Customer> findCustomersWithAtLeastOneSavingsAccount();
     */
}