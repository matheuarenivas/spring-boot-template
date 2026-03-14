package com.template.api.mapper;

import com.template.api.dto.request.CreateExampleRequest;
import com.template.api.dto.response.ExampleResponse;
import com.template.api.model.ExampleEntity;

/**
 * Converts between internal models and DTOs.
 * Static methods — no state, no Spring bean needed.
 */
public final class ExampleMapper {

    private ExampleMapper() {}

    /** Request DTO → internal model */
    public static ExampleEntity toEntity(CreateExampleRequest request) {
        ExampleEntity entity = new ExampleEntity();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setActive(true); // default new entities to active
        return entity;
    }

    /** Internal model → response DTO */
    public static ExampleResponse toResponse(ExampleEntity entity) {
        return new ExampleResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
}
