package com.Online.EcommerceApp.service;

import com.Online.EcommerceApp.dto.ProductRequest;
import com.Online.EcommerceApp.dto.ProductResponse;
import com.Online.EcommerceApp.model.Product;
import com.Online.EcommerceApp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest)
    {
        Product product=Product.builder().name(productRequest.getName()) .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("product {} saved sucessfully",product.getId());
    }

    public List<ProductResponse> getAllProduct() {
List<Product> products =productRepository.findAll();
return products.stream().map(this::mapToProductResponse).toList();

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice()).build();
    }
}
