CREATE TABLE suppliers (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    name VARCHAR(150) NOT NULL,

    contact_name VARCHAR(150),

    email VARCHAR(150),

    phone VARCHAR(30),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);