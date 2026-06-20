CREATE TABLE parts (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    name VARCHAR(150) NOT NULL,

    sku VARCHAR(100) UNIQUE NOT NULL,

    category VARCHAR(100),

    unit_price NUMERIC(10,2),

    active BOOLEAN NOT NULL DEFAULT TRUE

);