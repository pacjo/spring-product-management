package nodomain.cloudfide.task.service;

import lombok.RequiredArgsConstructor;
import nodomain.cloudfide.task.dto.request.CreateProductRequest;
import nodomain.cloudfide.task.dto.request.UpdateProductRequest;
import nodomain.cloudfide.task.dto.response.ProductPageResponse;
import nodomain.cloudfide.task.dto.response.ProductResponse;
import nodomain.cloudfide.task.entity.Producer;
import nodomain.cloudfide.task.entity.Product;
import nodomain.cloudfide.task.exception.ResourceNotFoundException;
import nodomain.cloudfide.task.mappers.ProductMapper;
import nodomain.cloudfide.task.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerService producerService;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductPageResponse getAllProducts(
        UUID producerId,
        String producerName,
        String productName,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Pageable pageable
    ) {
        Page<Product> page = productRepository.findByFilters(
            producerId, producerName, productName, minPrice, maxPrice, pageable
        );

        return new ProductPageResponse(
            page.getContent().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList()),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Producer producer = producerService.getProducerEntityById(request.getProducerId());

        Product product = productMapper.toEntity(request, producer);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Producer producer = null;
        if (request.getProducerId() != null) {
            producer = producerService.getProducerEntityById(request.getProducerId());
        }

        productMapper.updateEntity(product, request, producer);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductPageResponse getProductsByProducerId(UUID producerId, Pageable pageable) {
        // check producer exists
        producerService.getProducerById(producerId);

        Page<Product> page = productRepository.findByProducerId(producerId, pageable);

        return new ProductPageResponse(
            page.getContent().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList()),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}