package com.example.concurrency.Controller;

import com.example.concurrency.Model.Dto.ProductDto;
import com.example.concurrency.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public void createProduct(@ModelAttribute ProductDto dto){
        productService.createProduct(dto);
    }

    @GetMapping
    public List<ProductDto> searchProduct(){
        return productService.searchProduct();
    }

    @PatchMapping("/sysnchronized")
    public void updateProduct_Sysnchronized(@Param("id")Long id, @Param("qty")Long qty){
        productService.updateProduct_Sysnchronized(id, qty);
    }

    @PatchMapping("/pessimisticLocking")
    public void updateProduct_PessimisticLocking(@Param("id")Long id, @Param("qty")Long qty){
        productService.updateProduct_PessimisticLocking(id, qty);
    }
}
