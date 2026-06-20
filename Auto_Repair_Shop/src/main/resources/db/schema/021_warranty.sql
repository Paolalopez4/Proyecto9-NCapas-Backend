CREATE TABLE warranties (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    work_order_id UUID NOT NULL,


    start_date DATE,

    end_date DATE,


    coverage TEXT,


    active BOOLEAN DEFAULT TRUE,


    CONSTRAINT fk_warranty_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id)

);