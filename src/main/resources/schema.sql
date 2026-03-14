-- This file runs automatically on startup when spring.sql.init.mode=always
-- Perfect for H2 local dev. In production, use a migration tool like Flyway.

CREATE TABLE IF NOT EXISTS examples (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    email      VARCHAR(255)  NOT NULL UNIQUE,
    active     BOOLEAN       NOT NULL DEFAULT TRUE
);

-- Seed data for local development
INSERT INTO examples (name, email, active) VALUES ('Alice', 'alice@example.com', true);
INSERT INTO examples (name, email, active) VALUES ('Bob', 'bob@example.com', true);
INSERT INTO examples (name, email, active) VALUES ('Charlie', 'charlie@example.com', false);
