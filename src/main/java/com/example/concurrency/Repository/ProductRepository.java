package com.example.concurrency.Repository;

import com.example.concurrency.Model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("From Product WHERE id=:id")
    Product findProductByIdWithPessimisticLock(@Param("id") Long id);
}
