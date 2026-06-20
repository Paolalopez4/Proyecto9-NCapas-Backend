CREATE TABLE work_order_parts (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    work_order_id UUID NOT NULL,

    part_id UUID NOT NULL,


    quantity INTEGER NOT NULL DEFAULT 1,

    unit_price NUMERIC(10,2) NOT NULL,

    discount NUMERIC(10,2) DEFAULT 0,


    CONSTRAINT fk_wop_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id),


    CONSTRAINT fk_wop_part
        FOREIGN KEY(part_id)
            REFERENCES parts(id)

);