package com.example.concurrency.Repository;

import com.example.concurrency.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT get_lock(:key)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "SELECT release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
