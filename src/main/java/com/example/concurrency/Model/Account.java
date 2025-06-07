package com.example.concurrency.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    public Account(Long id, String account_number){
        this.account_number = account_number;
        this.balance = 0L;
        this.version = 0L;
    }
}
