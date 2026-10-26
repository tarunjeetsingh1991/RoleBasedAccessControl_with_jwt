package com.fdmgroup.springbootdemo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "CHECKING_ACCOUNT")
@PrimaryKeyJoinColumn(name = "ACCOUNT_ID")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "accountId")
public class CheckingAccount extends Account {

    @NotNull(message = "Next check number cannot be null")
    @Column(name = "NEXT_CHECK_NUMBER", nullable = false)
    private Integer nextCheckNumber;

    public CheckingAccount() {
    }

    public Integer getNextCheckNumber() {
        return nextCheckNumber;
    }

    public void setNextCheckNumber(Integer nextCheckNumber) {
        this.nextCheckNumber = nextCheckNumber;
    }
}