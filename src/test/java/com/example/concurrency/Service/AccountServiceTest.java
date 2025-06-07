package com.example.concurrency.Service;

import com.example.concurrency.Global.NamedAccountFacade;
import com.example.concurrency.Global.OptimistickLockAccountFacade;
import com.example.concurrency.Model.Dto.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OptimistickLockAccountFacade optimisticLockAccountFacade;

    @Autowired
    private NamedAccountFacade namedAccountFacade;

    private final Long ACCOUNT_ID = 1L;

    @BeforeEach
    void setUp() {
        accountService.deleteAll();

        AccountDto dto = AccountDto.builder()
                .id(ACCOUNT_ID)
                .account_number("123-456")
                .build();

        accountService.createAccount(dto);
    }

    @Test
    void optimistic_lock_동시성_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    optimisticLockAccountFacade.updateAccount(ACCOUNT_ID, 100L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long finalBalance = accountService.searchAccount().get(0).getBalance();
        assertThat(finalBalance).isEqualTo(100L * threadCount);
    }

    @Test
    void named_lock_동시성_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    namedAccountFacade.updateAccount_NamedLock(ACCOUNT_ID, 100L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long finalBalance = accountService.searchAccount().get(0).getBalance();
        assertThat(finalBalance).isEqualTo(100L * threadCount);
    }
}