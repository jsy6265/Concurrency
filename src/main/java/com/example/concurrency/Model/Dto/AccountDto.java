package com.example.concurrency.Model.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AccountDto {
    private Long id;
    private String account_number;
    private Long balance;
    private LocalDate create_at;
    private LocalDate update_at;
    private Long version;
}
