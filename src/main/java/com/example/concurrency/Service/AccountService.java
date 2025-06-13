package com.example.concurrency.Service;

import com.example.concurrency.Model.Account;
import com.example.concurrency.Model.Dto.AccountDto;
import com.example.concurrency.Repository.AccountRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(AccountDto dto) {
        Account account = new Account(dto.getAccount_number());

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Transactional
    public AccountDto findAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        return AccountDto.builder()
                .id(account.getId())
                .account_number(account.getAccount_number())
                .balance(account.getBalance())
                .version(account.getVersion())
                .build();
    }

    @Transactional
    public List<AccountDto> searchAccount() {
        return accountRepository.findAll().stream().map(account -> AccountDto.builder()
                .id(account.getId())
                .account_number(account.getAccount_number())
                .balance(account.getBalance())
                .version(account.getVersion())
                .build()).toList();
    }

    @Transactional
    public void updateAccount(Long id, Long balance) {
        Account account = accountRepository.findById(id).orElseThrow();

        account.updateBalance(balance);
    }

    // 3. Optimistic Locking
    // 낙관적 락
    // 버전 컬럼을 사용해서 읽어온 시섬과 트랜잭션이 커밋되는 시점의 데이터가 같은지 정합성을 비교
    // 버전이 다르면 에러 발생 -> catch문에서 잡아서 재귀로 다시 호출
    // 단점 : 충돌이 자주 발생할 떄를 로직을 처음부터 재시도하기 떄문에 매우 느리다, 재귀 방법으로 재시도할 경우 Stack Over Flow 발생할 수 있다.
    // 낙관적 락은 락을 어플리케이션 단에서 감지하는데 중점이고 update가 반드시 되야되는 것과는 상관없는듯?
    // 오히려 동시성 발생했을 떄 하나를 버리는게 목적같은데?
    @Transactional
    public void updateAccount_OptimisticLocking(Long id, Long balance){
        int retryCount = 0;
        int maxRetries = 5;
        while (retryCount < maxRetries) {
            try {
                Account account = accountRepository.findByIdWithOptimisticLock(id); // 최신 데이터 조회
                account.updateBalance(balance);
                accountRepository.saveAndFlush(account); // flush 시 버전 체크
                return;
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                retryCount++;
                try {
                    Thread.sleep(100); // 간단한 백오프
                } catch (InterruptedException ignored) {}
            }
        }
        throw new IllegalStateException("낙관적 락 충돌로 인해 업데이트 실패");
    }
}
