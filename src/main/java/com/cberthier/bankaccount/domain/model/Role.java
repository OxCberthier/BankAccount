package com.cberthier.bankaccount.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private RoleEnum name;

    //Default Hibernate constructor
    public Role() {
    }

    public Role(RoleEnum name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public RoleEnum getName() {
        return name;
    }
}
