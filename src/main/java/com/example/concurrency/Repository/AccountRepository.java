package com.example.concurrency.Repository;

import com.example.concurrency.Model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    @Query("FROM Account WHERE id=:id")
    Account findByIdWithOptimisticLock(@Param("id")Long id);


}
