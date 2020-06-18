package com.cberthier.bankaccount.domain.model;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_account_client_id"), nullable = false)
    private Client client;

    @Column(nullable = false)
    private double balance;

    //Default Hibernate constructor
    public Account() {
    }

    public Account(String name, Client client, double balance) {
        this.name = name;
        this.client = client;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Client getClient() {
        return client;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
