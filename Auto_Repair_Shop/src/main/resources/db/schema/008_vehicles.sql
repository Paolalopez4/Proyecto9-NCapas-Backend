CREATE TABLE vehicles (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    client_id UUID NOT NULL,

    plate VARCHAR(50) UNIQUE NOT NULL,

    brand VARCHAR(100),

    model VARCHAR(100),

    year INTEGER,

    color VARCHAR(50),

    vin VARCHAR(100) UNIQUE,

    mileage INTEGER DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_vehicle_client
        FOREIGN KEY(client_id)
            REFERENCES clients(id)

);