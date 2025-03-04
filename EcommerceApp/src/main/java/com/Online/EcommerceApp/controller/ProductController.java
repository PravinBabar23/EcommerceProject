package com.Online.EcommerceApp.controller;

import com.Online.EcommerceApp.dto.ProductRequest;
import com.Online.EcommerceApp.dto.ProductResponse;
import com.Online.EcommerceApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest)
    {
        productService.createProduct(productRequest);
    }

    //getallAPI
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse>getAllProduct()
    {
      return productService.getAllProduct();

    }

}
