package com.fdmgroup.springbootdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.springbootdemo.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}