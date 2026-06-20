CREATE TABLE invoices (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    work_order_id UUID NOT NULL,


    invoice_number VARCHAR(100)
        UNIQUE NOT NULL,


    subtotal NUMERIC(12,2),

    tax NUMERIC(12,2),

    total NUMERIC(12,2),


    status VARCHAR(30) NOT NULL,


    issued_at TIMESTAMP,

    paid_at TIMESTAMP,


    CONSTRAINT invoice_status_check
        CHECK(status IN(
                        'ISSUED',
                        'PAID',
                        'CANCELLED'
            )),


    CONSTRAINT fk_invoice_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id)

);