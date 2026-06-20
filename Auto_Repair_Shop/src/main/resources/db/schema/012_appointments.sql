CREATE TABLE appointments (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    scheduled_at TIMESTAMP NOT NULL,

    status VARCHAR(30) NOT NULL,

    notes TEXT,


    client_id UUID NOT NULL,

    vehicle_id UUID NOT NULL,

    branch_id UUID NOT NULL,

    mechanic_id UUID,


    CONSTRAINT appointment_status_check
        CHECK(status IN(
                        'PENDING',
                        'CONFIRMED',
                        'CANCELLED',
                        'DONE'
            )),


    CONSTRAINT fk_appointment_client
        FOREIGN KEY(client_id)
            REFERENCES clients(id),


    CONSTRAINT fk_appointment_vehicle
        FOREIGN KEY(vehicle_id)
            REFERENCES vehicles(id),


    CONSTRAINT fk_appointment_branch
        FOREIGN KEY(branch_id)
            REFERENCES branches(id),


    CONSTRAINT fk_appointment_mechanic
        FOREIGN KEY(mechanic_id)
            REFERENCES mechanics(id)

);