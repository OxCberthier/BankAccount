package com.cberthier.bankaccount.domain.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String LastName;

    @Column(nullable = false)
    private String FirstName;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Account> accounts;

    //Default Hibernate constructor
    public Client() {
    }

    public Client(String lastName, String firstName) {
        LastName = lastName;
        FirstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }

        accounts.add(account);
    }
}
