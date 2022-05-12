package com.jam.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Defines a Reimbursement request by an employee user.
 */
@Entity
@Table(name = "reimbursements")
@Getter
@Setter
public class Reimbursement {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "MM/dd/yyyy")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate requestdate;
    private String description;
    private BigDecimal amount;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean approved;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Reimbursement() {
    }

    public Reimbursement(LocalDate requestdate, String description, BigDecimal amount, User user) {
        this.requestdate = requestdate;
        this.description = description;
        this.amount = amount;
        this.user = user;
    }
}
