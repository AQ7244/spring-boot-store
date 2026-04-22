package com.training.store.products.controllers;

import com.training.store.products.dtos.ProductDto;
import com.training.store.products.entities.Category;
import com.training.store.products.entities.Product;
import com.training.store.products.mappers.ProductMapper;
import com.training.store.products.repositories.CategoryRepository;
import com.training.store.products.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Operations related to products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all active products available to users."
    )
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam(defaultValue = "", required = false, name = "categoryId")
            String categoryIdValue
    ) {
        List<Product> products;
        if (!categoryIdValue.isEmpty()) {

            try {
                byte categoryId = Byte.parseByte(categoryIdValue);
//                System.out.println("Here is categoryId " + categoryId);
                products = productRepository.findProductsByCategory(new Category(categoryId));
            } catch (Exception exception) {
                return  ResponseEntity.notFound().build();
            }
        } else {

            products = productRepository.findAllWithCategory();
        }

        return ResponseEntity.ok(products
                .stream()
                .map(productMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a product by ID",
            description = "Retrieve information about a product using its identifier."
    )
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    @Operation(
            summary = "Create a product",
            description = "Create a new product by providing required product details in the request body."
    )
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriComponentsBuilder
            ) {

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var newProduct = productMapper.toEntity(productDto);
        newProduct.setCategory(category);
        productRepository.save(newProduct);

        productDto.setId(newProduct.getId());
        var productUri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(productUri).body(productDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a product",
            description = "Update an existing product using its ID and provided details."
    )
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long productId,
            @RequestBody ProductDto productDto
            ) {

        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        productMapper.updateProduct(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product",
            description = "Delete a product permanently by specifying its unique ID."
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(productId);

        return ResponseEntity.noContent().build();
    }
}
