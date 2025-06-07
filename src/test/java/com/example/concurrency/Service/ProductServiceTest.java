package com.example.concurrency.Service;

import com.example.concurrency.Model.Dto.ProductDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @BeforeEach
    public void before(){
        ProductDto dto = ProductDto.builder().id(1l).name("phone").qty(100l).build();
        productService.createProduct(dto);
    }

    @AfterEach
    public void after(){
        productService.deleteAll();
    }

    @Test
    @DisplayName("Sysnchronized 테스트")
    public void SysnchronizedTest() throws InterruptedException{
        int threadCount = 100;

        // 멀티 스레드
        // ExecutorService : 비동기 처리할 수 있게 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행이 완료될 떄 까지 대기할 수 있도록 도와주는 api
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.updateProduct_Sysnchronized(1l, 1l);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<ProductDto> list = productService.searchProduct();

        assertThat(list.get(0).getQty()).isEqualTo(200);
    }

    @Test
    @DisplayName("PessimisticLocking 테스트")
    public void PessimisticLockingTest() throws InterruptedException{
        int threadCount = 100;

        // 멀티 스레드
        // ExecutorService : 비동기 처리할 수 있게 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 스레드에서 수행이 완료될 떄 까지 대기할 수 있도록 도와주는 api
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.updateProduct_PessimisticLocking(1l, 1l);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<ProductDto> list = productService.searchProduct();

        assertThat(list.get(0).getQty()).isEqualTo(200);
    }
}