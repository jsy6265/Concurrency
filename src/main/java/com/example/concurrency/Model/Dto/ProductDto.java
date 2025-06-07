package com.example.concurrency.Model.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private Long qty;
    private LocalDate create_at;
    private LocalDate update_at;
}
