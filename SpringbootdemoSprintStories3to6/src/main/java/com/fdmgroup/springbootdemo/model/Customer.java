
package com.fdmgroup.springbootdemo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "CUSTOMER")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
        name = "CUSTOMER_TYPE",
        discriminatorType = DiscriminatorType.STRING
)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "customerId"
)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "customerType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Person.class, name = "PERSON"),
        @JsonSubTypes.Type(value = Company.class, name = "COMPANY")
})
public abstract class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @NotBlank(message = "Customer name cannot be blank")
    @Column(name = "CUSTOMER_NAME", nullable = false, updatable = false)
    private String customerName;

    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ADDRESS_ID", nullable = false)
    private Address address;
    // customer is the owner for customer - address

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();
    // customer is the non - owning side for customer - account
    
    public Customer() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Address getAddress() {
        return address;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    // Helper for Story 2 convenience
    public String getPostalCode() {
        return address != null ? address.getPostalCode() : null;
    }

    // Helper to maintain bidirectional relationship
    // This keeps both sides in sync, and most importantly, updates the owning side.
    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setCustomer(this);
    }
}
