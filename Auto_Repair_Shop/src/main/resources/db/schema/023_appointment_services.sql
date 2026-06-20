CREATE TABLE appointment_services (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    appointment_id UUID NOT NULL,

    service_id UUID NOT NULL,

    CONSTRAINT fk_appointment_service_appointment
        FOREIGN KEY(appointment_id)
            REFERENCES appointments(id),

    CONSTRAINT fk_appointment_service_service
        FOREIGN KEY(service_id)
            REFERENCES services(id),

    CONSTRAINT unique_appointment_service
        UNIQUE(appointment_id, service_id)

);