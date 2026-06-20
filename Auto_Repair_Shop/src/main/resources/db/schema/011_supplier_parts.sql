CREATE TABLE supplier_parts (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    supplier_id UUID NOT NULL,

    part_id UUID NOT NULL,

    price NUMERIC(10,2) NOT NULL,

    lead_time_days INTEGER,

    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_supplier_part_supplier
        FOREIGN KEY(supplier_id)
            REFERENCES suppliers(id),


    CONSTRAINT fk_supplier_part_part
        FOREIGN KEY(part_id)
            REFERENCES parts(id),


    CONSTRAINT unique_supplier_part
        UNIQUE(
               supplier_id,
               part_id
            )

);