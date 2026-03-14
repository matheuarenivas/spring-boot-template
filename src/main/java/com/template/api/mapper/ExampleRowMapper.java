package com.template.api.mapper;

import com.template.api.model.ExampleEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps database rows to ExampleEntity objects.
 * Extracted as a @Component so it can be injected and reused
 * across multiple repositories if needed.
 */
@Component
public class ExampleRowMapper implements RowMapper<ExampleEntity> {

    @Override
    public ExampleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExampleEntity entity = new ExampleEntity();
        entity.setId(rs.getLong("id"));
        entity.setName(rs.getString("name"));
        entity.setEmail(rs.getString("email"));
        entity.setActive(rs.getBoolean("active"));
        return entity;
    }
}
