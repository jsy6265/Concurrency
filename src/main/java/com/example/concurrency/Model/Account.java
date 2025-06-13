package com.example.concurrency.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account_number;
    private Long balance;
    @Version
    private Long version;

    public void updateBalance(Long balance){
        this.balance += balance;
    }

    public Account(String account_number){
        this.account_number = account_number;
        this.balance = 0L;
    }
}
