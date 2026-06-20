CREATE TABLE maintenance_reminders (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    reminder_type VARCHAR(30) NOT NULL,

    due_date DATE,

    due_mileage INTEGER,

    sent BOOLEAN NOT NULL DEFAULT FALSE,

    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,

    notes TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    vehicle_id UUID NOT NULL,

    client_id UUID NOT NULL,

    CONSTRAINT reminder_type_check
        CHECK(reminder_type IN (
                                'OIL_CHANGE',
                                'BRAKES',
                                'REVISION',
                                'TIRES',
                                'OTHER'
            )),

    CONSTRAINT fk_reminder_vehicle
        FOREIGN KEY(vehicle_id)
            REFERENCES vehicles(id),

    CONSTRAINT fk_reminder_client
        FOREIGN KEY(client_id)
            REFERENCES clients(id)

);