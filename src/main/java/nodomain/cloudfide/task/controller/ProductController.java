package nodomain.cloudfide.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nodomain.cloudfide.task.dto.request.CreateProductRequest;
import nodomain.cloudfide.task.dto.request.UpdateProductRequest;
import nodomain.cloudfide.task.dto.response.ProductPageResponse;
import nodomain.cloudfide.task.dto.response.ProductResponse;
import nodomain.cloudfide.task.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static nodomain.cloudfide.task.utils.PageableUtils.createPageable;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductPageResponse> getAllProducts(
        @RequestParam(required = false) UUID producerId,
        @RequestParam(required = false) String producerName,
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String sort
    ) {
        Pageable pageable = createPageable(page, size, sort);
        ProductPageResponse response = productService.getAllProducts(
            producerId, producerName, productName, minPrice, maxPrice, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateProductRequest request
    ) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}