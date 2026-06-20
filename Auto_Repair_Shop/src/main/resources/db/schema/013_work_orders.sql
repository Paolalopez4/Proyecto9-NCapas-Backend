CREATE TABLE work_orders (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    appointment_id UUID,

    vehicle_id UUID NOT NULL,

    mechanic_id UUID,

    branch_id UUID,


    order_type VARCHAR(40) NOT NULL,

    status VARCHAR(40) NOT NULL,


    diagnosis TEXT,


    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    closed_at TIMESTAMP,


    CONSTRAINT work_order_type_check
        CHECK(order_type IN(
                            'STANDARD',
                            'EXPRESS',
                            'INSURANCE',
                            'POST_WARRANTY'
            )),


    CONSTRAINT work_order_status_check
        CHECK(status IN(
                        'OPEN',
                        'IN_PROGRESS',
                        'AWAITING_APPROVAL',
                        'DONE',
                        'CANCELLED'
            )),


    CONSTRAINT fk_work_order_vehicle
        FOREIGN KEY(vehicle_id)
            REFERENCES vehicles(id),


    CONSTRAINT fk_work_order_mechanic
        FOREIGN KEY(mechanic_id)
            REFERENCES mechanics(id),


    CONSTRAINT fk_work_order_branch
        FOREIGN KEY(branch_id)
            REFERENCES branches(id),


    CONSTRAINT fk_work_order_appointment
        FOREIGN KEY(appointment_id)
            REFERENCES appointments(id)

);