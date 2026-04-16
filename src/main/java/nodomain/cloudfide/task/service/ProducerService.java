package nodomain.cloudfide.task.service;

import lombok.RequiredArgsConstructor;
import nodomain.cloudfide.task.dto.request.CreateProducerRequest;
import nodomain.cloudfide.task.dto.request.UpdateProducerRequest;
import nodomain.cloudfide.task.dto.response.ProducerPageResponse;
import nodomain.cloudfide.task.dto.response.ProducerResponse;
import nodomain.cloudfide.task.entity.Producer;
import nodomain.cloudfide.task.exception.ProducerHasProductsException;
import nodomain.cloudfide.task.exception.ResourceNotFoundException;
import nodomain.cloudfide.task.mappers.ProducerMapper;
import nodomain.cloudfide.task.repository.ProducerRepository;
import nodomain.cloudfide.task.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;
    private final ProducerMapper producerMapper;

    @Transactional(readOnly = true)
    public ProducerPageResponse getAllProducers(String name, Pageable pageable) {
        Page<Producer> page;
        if (name != null && !name.isBlank())
            page = producerRepository.findByNameContainingIgnoreCase(name, pageable);
        else
            page = producerRepository.findAll(pageable);

        return new ProducerPageResponse(
            page.getContent().stream()
                .map(producerMapper::toResponse)
                .collect(Collectors.toList()),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public ProducerResponse getProducerById(UUID id) {
        Producer producer = producerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found with id: " + id));
        return producerMapper.toResponse(producer);
    }

    @Transactional
    public ProducerResponse createProducer(CreateProducerRequest request) {
        Producer producer = producerMapper.toEntity(request);
        Producer savedProducer = producerRepository.save(producer);
        return producerMapper.toResponse(savedProducer);
    }

    @Transactional
    public ProducerResponse updateProducer(UUID id, UpdateProducerRequest request) {
        Producer producer = producerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found with id: " + id));

        producerMapper.updateEntity(producer, request);
        Producer updatedProducer = producerRepository.save(producer);
        return producerMapper.toResponse(updatedProducer);
    }

    @Transactional
    public void deleteProducer(UUID id) {
        Producer producer = producerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found with id: " + id));

        if (productRepository.countByProducerId(id) > 0)
            throw new ProducerHasProductsException("Cannot delete. Producer has associated products.");

        producerRepository.delete(producer);
    }

    @Transactional(readOnly = true)
    public Producer getProducerEntityById(UUID id) {
        return producerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producer not found with id: " + id));
    }
}