package com.template.api.repository;

import com.template.api.mapper.ExampleRowMapper;
import com.template.api.model.ExampleEntity;
import com.template.api.query.ExampleQueries;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExampleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ExampleRowMapper rowMapper;

    public ExampleRepository(JdbcTemplate jdbcTemplate, ExampleRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public List<ExampleEntity> findAll() {
        return jdbcTemplate.query(ExampleQueries.FIND_ALL, rowMapper);
    }

    /**
     * Returns Optional instead of throwing an exception when not found.
     * Safer than queryForObject which throws EmptyResultDataAccessException.
     */
    public Optional<ExampleEntity> findById(Long id) {
        List<ExampleEntity> results = jdbcTemplate.query(
                ExampleQueries.FIND_BY_ID, rowMapper, id
        );
        return results.stream().findFirst();
    }

    public List<ExampleEntity> findByActive(boolean active) {
        return jdbcTemplate.query(ExampleQueries.FIND_BY_ACTIVE, rowMapper, active);
    }

    public int save(ExampleEntity entity) {
        return jdbcTemplate.update(
                ExampleQueries.INSERT,
                entity.getName(),
                entity.getEmail(),
                entity.isActive()
        );
    }

    public int update(ExampleEntity entity) {
        return jdbcTemplate.update(
                ExampleQueries.UPDATE,
                entity.getName(),
                entity.getEmail(),
                entity.isActive(),
                entity.getId()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(ExampleQueries.DELETE_BY_ID, id);
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                ExampleQueries.EXISTS_BY_EMAIL, Integer.class, email
        );
        return count != null && count > 0;
    }
}
