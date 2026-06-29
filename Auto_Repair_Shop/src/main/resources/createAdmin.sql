-- Seed default ADMIN user
-- Default credentials:
-- Email: admin@test.com
-- Password: Password123
-- Copiar el codigo y ejecutarlo en pgAdmin4 para crear al Admin

CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS token_version BIGINT DEFAULT 0;

INSERT INTO users (
    id,
    name,
    email,
    password_hash,
    role,
    active,
    token_version,
    created_at
)
VALUES (
           gen_random_uuid(),
           'Default Admin',
           'admin@test.com',
           '$2y$10$Z2vFjK9pGR1oErgyP2Eqz.jhgiTLpDwwmGv9mt0trPjrALDOWVUsi',
           'ADMIN',
           true,
           0,
           NOW()
       )
ON CONFLICT (email) DO UPDATE
    SET
        role = 'ADMIN',
        active = true,
        token_version = 0;