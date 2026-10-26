package com.fdmgroup.springbootdemo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SAVINGS_ACCOUNT")
@PrimaryKeyJoinColumn(name = "ACCOUNT_ID")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "accountId")
public class SavingsAccount extends Account {

    @NotNull(message = "Interest rate cannot be null")
    @Column(name = "INTEREST_RATE", nullable = false)
    private Double interestRate;

    public SavingsAccount() {
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }
}