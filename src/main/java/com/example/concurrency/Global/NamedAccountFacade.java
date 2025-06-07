package com.example.concurrency.Global;

import com.example.concurrency.Model.Account;
import com.example.concurrency.Repository.LockRepository;
import com.example.concurrency.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedAccountFacade {
    private final LockRepository lockRepository;
    private final AccountService accountService;

    // 4. Named Locking
    // MySql에서 제공하는 기능, 특정 문자열에 대해서 락을 획득하는 방법
    // 여러 분산된 공유 자원들을 하나의 락으로 관리하기 위해 분산락을 사용
    // 락 테이블을 따로 관리하는 방법
    // 단점 : 락을 얻기 위해 DB 커넥션 스레드가 대기(스레드 고갈 발생할 수 있음), 락을 풀지 않으면 더이상 상용 불가
    // 여러 테이블을 대상으로 락을 걸어야하는데 키가 숫자이거나, 겹치면?
    @Transactional
    public void updateAccount_NamedLock(final Long id, Long balance){
        try {
            lockRepository.getLock(id.toString());
            accountService.updateAccount(id, balance);
        }finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
