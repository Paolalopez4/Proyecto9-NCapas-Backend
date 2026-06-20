CREATE TABLE work_order_services (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    work_order_id UUID NOT NULL,

    service_id UUID NOT NULL,


    quantity INTEGER NOT NULL DEFAULT 1,

    unit_price NUMERIC(10,2) NOT NULL,

    discount NUMERIC(10,2) DEFAULT 0,


    CONSTRAINT fk_wos_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id),


    CONSTRAINT fk_wos_service
        FOREIGN KEY(service_id)
            REFERENCES services(id)

);