CREATE TABLE clients (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    user_id UUID NOT NULL,

    phone VARCHAR(30),

    address VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_client_user
        FOREIGN KEY(user_id)
            REFERENCES users(id)
);