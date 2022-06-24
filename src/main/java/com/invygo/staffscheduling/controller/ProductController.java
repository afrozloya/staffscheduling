package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.models.Product;
import com.invygo.staffscheduling.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/api/products")
    @RolesAllowed("ADMIN")
    public Iterable<Product> product() {
        return productRepository.findAll();
    }

    @PostMapping(value = "/api/products")
    public String save(@RequestBody Product product) {
        productRepository.save(product);

        return product.getId();
    }

    @RolesAllowed("USER")
    @GetMapping("/api/products/{id}")
    public Optional<Product> show(@PathVariable String id) {
        return productRepository.findById(id);
    }

    @PutMapping("/api/products/{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        Optional<Product> prod = productRepository.findById(id);
        if (product.getProdName() != null)
            prod.get().setProdName(product.getProdName());
        if (product.getProdDesc() != null)
            prod.get().setProdDesc(product.getProdDesc());
        if (product.getProdPrice() != null)
            prod.get().setProdPrice(product.getProdPrice());
        if (product.getProdImage() != null)
            prod.get().setProdImage(product.getProdImage());
        productRepository.save(prod.get());
        return prod.get();
    }

    @DeleteMapping("/api/products/{id}")
    public String delete(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        productRepository.delete(product.get());

        return "product deleted";
    }

}
