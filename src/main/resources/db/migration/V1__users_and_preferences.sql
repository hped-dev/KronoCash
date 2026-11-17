-- V1: Usuarios y preferencias

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(50) NOT NULL DEFAULT 'minimal',
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    language VARCHAR(10) NOT NULL DEFAULT 'es',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_preferences_user UNIQUE (user_id)
);

-- Usuario por defecto para poder navegar las rutas MVC sin configurar login externo
-- Usuario: kronocash / krono123
INSERT INTO users (username, email, password, role, created_at)
VALUES ('kronocash', 'kronocash@kronocash.local',
        '$2a$10$K9.eK1Yk9Yk9Yk9Yk9Yk9uK9Yk9Yk9Yk9Yk9Yk9Yk9Yk9Yk9Yk9',
        'USER', CURRENT_TIMESTAMP);

INSERT INTO preferences (user_id, theme, currency, language, updated_at)
VALUES (1, 'minimal', 'USD', 'es', CURRENT_TIMESTAMP);
