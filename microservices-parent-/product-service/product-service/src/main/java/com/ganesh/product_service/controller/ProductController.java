package com.ganesh.product_service.controller;

import com.ganesh.product_service.dto.ProductRequest;
import com.ganesh.product_service.dto.ProductResponse;
import com.ganesh.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void createProduct(@RequestBody ProductRequest productRequest){
productService.createProduct(productRequest);
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return  new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

}
