CREATE TABLE hour_logs (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    work_order_id UUID NOT NULL,

    mechanic_id UUID NOT NULL,


    hours NUMERIC(5,2) NOT NULL,


    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,


    notes TEXT,


    CONSTRAINT fk_hour_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id),


    CONSTRAINT fk_hour_mechanic
        FOREIGN KEY(mechanic_id)
            REFERENCES mechanics(id)

);