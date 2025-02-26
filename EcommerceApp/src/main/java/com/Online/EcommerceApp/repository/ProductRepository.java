package com.Online.EcommerceApp.repository;

import com.Online.EcommerceApp.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
