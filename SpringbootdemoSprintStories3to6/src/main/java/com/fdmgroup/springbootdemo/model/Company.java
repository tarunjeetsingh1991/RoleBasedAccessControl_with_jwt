package com.fdmgroup.springbootdemo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPANY")
@DiscriminatorValue("COMPANY")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "customerId")
public class Company extends Customer {

    public Company() {
    }
}