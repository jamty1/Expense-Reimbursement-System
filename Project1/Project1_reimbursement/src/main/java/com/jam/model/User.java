package com.jam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a User in the application.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType usertype;

    @Column(nullable = false)
    private boolean notify = true;

    private String apikey;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reimbursement> reimbursements = new ArrayList<>();

    public User() {
    }
    public User(String name, String password, String email, UserType usertype) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.usertype = usertype;
    }
}
