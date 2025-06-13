package com.example.concurrency.Controller;

import com.example.concurrency.Global.NamedAccountFacade;
import com.example.concurrency.Model.Dto.AccountDto;
import com.example.concurrency.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final NamedAccountFacade namedAccountFacade;

    @PostMapping
    public void createProduct(@ModelAttribute AccountDto dto){
        accountService.createAccount(dto);
    }

    @GetMapping
    public List<AccountDto> searchProduct(){
        return accountService.searchAccount();
    }

    @PatchMapping("/optimisticLocking")
    public void updateAccount_OptimisticLocking(Long id, Long balance){
        accountService.updateAccount_OptimisticLocking(id, balance);
    }

    @PatchMapping("/namedLocking")
    public void updateAccount_NamedLock(Long id, Long balance){
        namedAccountFacade.updateAccount_NamedLock(id, balance);
    }
}
