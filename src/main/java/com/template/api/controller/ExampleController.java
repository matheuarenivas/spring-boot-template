package com.template.api.controller;

import com.template.api.dto.request.CreateExampleRequest;
import com.template.api.dto.response.ExampleResponse;
import com.template.api.service.ExampleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examples")
public class ExampleController {

    private final ExampleService service;

    public ExampleController(ExampleService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExampleResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ExampleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * @Valid triggers the validation annotations on CreateExampleRequest.
     * If validation fails, GlobalExceptionHandler catches it and returns 400.
     */
    @PostMapping
    public ResponseEntity<ExampleResponse> create(@Valid @RequestBody CreateExampleRequest request) {
        ExampleResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
