package com.example.concurrency.Global;

import com.example.concurrency.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimistickLockAccountFacade {
    private final AccountService accountService;

    public void updateAccount(Long id, Long balance) {
        while (true) {
            try {
                accountService.updateAccount_OptimisticLocking(id, balance);
                break;
            } catch (final OptimisticLockingFailureException e) {
                try {
                    Thread.sleep(50); // 백오프
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
