CREATE TABLE services (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    name VARCHAR(150) NOT NULL,

    description TEXT,

    base_price NUMERIC(10,2) NOT NULL,

    estimated_minutes INTEGER,

    active BOOLEAN NOT NULL DEFAULT TRUE

);