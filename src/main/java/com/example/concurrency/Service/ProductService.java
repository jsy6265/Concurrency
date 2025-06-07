package com.example.concurrency.Service;

import com.example.concurrency.Model.Dto.ProductDto;
import com.example.concurrency.Model.Product;
import com.example.concurrency.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(ProductDto dto){
        Product product = new Product(dto.getId(), dto.getName(), dto.getQty());

        productRepository.save(product);
    }

    public List<ProductDto> searchProduct() {
        return productRepository.findAll().stream().map(product -> ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .qty(product.getQty())
                .create_at(product.getCreate_at())
                .update_at(product.getUpdate_at())
                .build()).toList();
    }

    @Transactional
    public void updateProduct(Long id, Long qty){
        Product product = productRepository.findById(id).orElseThrow();

        product.updateQty(qty);
    }

    @Transactional
    public void deleteAll() {
        productRepository.deleteAll();
    }

    // 1. Sysnchronized
    // 해당 영역을 임계영역으로 만들고 하나의 스레드만 접근 가능하게 함
    // 락 잡힌 상태로 들어온 요청은 처리안됨
    // 단점
    //  1. JPA를 사용하면 실제 DB로 flush 되는 시점을 정확히 알 수 없다
    //  2. 서버가 scale out 되면 동시에 메서드 호출이 가능하다.
    @Transactional
    public void updateProduct_Sysnchronized(Long id, Long qty){
        final Product product = productRepository.findById(id).orElseThrow();

        product.updateQty(qty);

        productRepository.saveAndFlush(product);
    }
    
    // 2. Pessimistic Locking
    // 비관적 락
    // 조회 후 락걸려있으면 대기하고 처리함
    // 처리 시간 지연됨
    // 단점
    //  1. 항상 배타락을 걸고 사용하기 떄문에 기존보다 수행 시간을 늘어난다
    //  2. MVCC를 통해 데이터를 읽을 떄에 비해 데드락 발생 확률이 늘어난다
    //  3. 다른 요청으로 들어온 DB 세션들이 기다리는 확률이 늘어났기 떄문에 스레드 고갈 발생할 수 있음
    @Transactional
    public void updateProduct_PessimisticLocking(Long id, Long qty){
        final Product product = productRepository.findProductByIdWithPessimisticLock(id);

        product.updateQty(qty);
    }
}