package com.template.api.service;

import com.template.api.dto.request.CreateExampleRequest;
import com.template.api.dto.response.ExampleResponse;
import com.template.api.exception.BadRequestException;
import com.template.api.exception.ResourceNotFoundException;
import com.template.api.mapper.ExampleMapper;
import com.template.api.model.ExampleEntity;
import com.template.api.repository.ExampleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExampleService {

    private final ExampleRepository repository;

    public ExampleService(ExampleRepository repository) {
        this.repository = repository;
    }

    public List<ExampleResponse> getAll() {
        return repository.findAll().stream()
                .map(ExampleMapper::toResponse)  // converts each entity → response DTO
                .collect(Collectors.toList());
    }

    public ExampleResponse getById(Long id) {
        ExampleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Example", id));
        return ExampleMapper.toResponse(entity);
    }

    public ExampleResponse create(CreateExampleRequest request) {
        // Business rule: no duplicate emails
        if (repository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        ExampleEntity entity = ExampleMapper.toEntity(request);
        repository.save(entity);

        // In a real app you'd return the entity with its generated ID.
        // For simplicity, we return what we have.
        return ExampleMapper.toResponse(entity);
    }

    public void deleteById(Long id) {
        // Verify it exists before deleting
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Example", id));
        repository.deleteById(id);
    }
}
