package com.template.api.model;

/**
 * Example entity — replace with your own domain model.
 * No JPA annotations needed when using JdbcTemplate.
 */
public class ExampleEntity {

    private Long id;
    private String name;
    private String email;
    private boolean active;

    public ExampleEntity() {}

    public ExampleEntity(Long id, String name, String email, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = active;
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
