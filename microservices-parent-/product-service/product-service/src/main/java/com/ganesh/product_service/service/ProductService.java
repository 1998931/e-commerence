package com.ganesh.product_service.service;

import com.ganesh.product_service.dto.ProductRequest;
import com.ganesh.product_service.dto.ProductResponse;
import com.ganesh.product_service.model.Product;
import com.ganesh.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public void createProduct(ProductRequest productRequest) {
    Product product = Product.builder()
            .name(productRequest.getName())
            .price(productRequest.getPrice())
            .description(productRequest.getDescription())
            .build();
    productRepository.save(product);
log.info("Product {} saved",product.getId());


}
public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
    List<ProductResponse> list = products.stream().map(this::mapToProductResponse).toList();
    return list;
}
private ProductResponse mapToProductResponse(Product product) {
        return  ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
}
}
