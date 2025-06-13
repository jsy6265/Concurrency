package com.example.concurrency.Service;

import com.example.concurrency.Global.NamedAccountFacade;
import com.example.concurrency.Model.Dto.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private NamedAccountFacade namedAccountFacade;

    private final Long ACCOUNT_ID = 1L;

    @BeforeEach
    void before() {
        accountService.deleteAll();

        AccountDto dto = AccountDto.builder()
                .account_number("123-456")
                .build();

        accountService.createAccount(dto);
    }

    @Test
    void optimistickLockTest() throws InterruptedException {
        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int reqNum = i;
            executor.submit(() -> {
                try {
                    accountService.updateAccount_OptimisticLocking(ACCOUNT_ID, 100L);
                } catch (Exception e) {
                    System.out.println("쓰레드 예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        AccountDto finalVersion = accountService.findAccountById(ACCOUNT_ID);
        System.out.println("최종 버전: " + finalVersion);
        System.out.println(finalVersion.getBalance());
        assertThat(finalVersion.getVersion()).isEqualTo(3L);
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