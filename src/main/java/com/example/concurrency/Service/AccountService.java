package com.example.concurrency.Service;

import com.example.concurrency.Model.Account;
import com.example.concurrency.Model.Dto.AccountDto;
import com.example.concurrency.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(AccountDto dto) {
        Account account = new Account(dto.getId(), dto.getAccount_number());

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Transactional
    public List<AccountDto> searchAccount() {
        return accountRepository.findAll().stream().map(account -> AccountDto.builder()
                .id(account.getId())
                .account_number(account.getAccount_number())
                .balance(account.getBalance())
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
    @Transactional
    public void updateAccount_OptimisticLocking(Long id, Long balance){
        final Account account = accountRepository.findByIdWithOptimisticLock(id);

        account.updateBalance(balance);
    }
}
