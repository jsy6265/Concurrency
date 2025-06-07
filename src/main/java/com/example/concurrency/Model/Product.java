package com.example.concurrency.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    private Long id;
    private String name;
    private Long qty;
    @CreatedDate
    private LocalDate create_at;
    @LastModifiedDate
    private LocalDate update_at;

    public void updateQty(Long qty){
        this.qty += qty;
    }

    public Product(Long id, String name, Long qty){
        this.id = id;
        this.name = name;
        this.qty = qty;
    }
}
