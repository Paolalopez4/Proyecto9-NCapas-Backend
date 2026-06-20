CREATE TABLE repair_history (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    vehicle_id UUID NOT NULL,

    work_order_id UUID NOT NULL,


    summary TEXT,


    repair_date DATE,


    mileage_at_repair INTEGER,


    CONSTRAINT fk_history_vehicle
        FOREIGN KEY(vehicle_id)
            REFERENCES vehicles(id),


    CONSTRAINT fk_history_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id)

);