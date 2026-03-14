package com.template.api.query;

/**
 * Centralized SQL queries for ExampleEntity.
 * One file per entity keeps things organized as the project grows.
 */
public final class ExampleQueries {

    private ExampleQueries() {}

    public static final String FIND_ALL =
            "SELECT id, name, email, active FROM examples";

    public static final String FIND_BY_ID =
            "SELECT id, name, email, active FROM examples WHERE id = ?";

    public static final String FIND_BY_ACTIVE =
            "SELECT id, name, email, active FROM examples WHERE active = ?";

    public static final String INSERT =
            "INSERT INTO examples (name, email, active) VALUES (?, ?, ?)";

    public static final String UPDATE =
            "UPDATE examples SET name = ?, email = ?, active = ? WHERE id = ?";

    public static final String DELETE_BY_ID =
            "DELETE FROM examples WHERE id = ?";

    public static final String EXISTS_BY_EMAIL =
            "SELECT COUNT(*) FROM examples WHERE email = ?";
}
