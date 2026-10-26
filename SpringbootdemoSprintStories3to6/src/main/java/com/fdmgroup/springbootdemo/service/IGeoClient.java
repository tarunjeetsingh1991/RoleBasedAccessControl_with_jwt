package com.fdmgroup.springbootdemo.service;

import com.fdmgroup.springbootdemo.model.Customer;

public interface IGeoClient 
{
    Customer enrichCustomerWithCityProvince(Customer customer);
}