package nodomain.cloudfide.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nodomain.cloudfide.task.dto.request.CreateProducerRequest;
import nodomain.cloudfide.task.dto.request.UpdateProducerRequest;
import nodomain.cloudfide.task.dto.response.ProducerPageResponse;
import nodomain.cloudfide.task.dto.response.ProducerResponse;
import nodomain.cloudfide.task.dto.response.ProductPageResponse;
import nodomain.cloudfide.task.service.ProducerService;
import nodomain.cloudfide.task.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nodomain.cloudfide.task.utils.PageableUtils.createPageable;

@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProducerPageResponse> getAllProducers(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String sort
    ) {
        Pageable pageable = createPageable(page, size, sort);
        ProducerPageResponse response = producerService.getAllProducers(name, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponse> getProducerById(@PathVariable UUID id) {
        ProducerResponse response = producerService.getProducerById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProducerResponse> createProducer(@Valid @RequestBody CreateProducerRequest request) {
        ProducerResponse response = producerService.createProducer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProducerResponse> updateProducer(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateProducerRequest request
    ) {
        ProducerResponse response = producerService.updateProducer(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducer(@PathVariable UUID id) {
        producerService.deleteProducer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ProductPageResponse> getProducerProducts(
        @PathVariable UUID id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ProductPageResponse response = productService.getProductsByProducerId(id, pageable);
        return ResponseEntity.ok(response);
    }
}