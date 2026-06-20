CREATE TABLE budgets (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    work_order_id UUID NOT NULL,


    tax_rate NUMERIC(5,2),

    total NUMERIC(12,2),


    status VARCHAR(30) NOT NULL,


    sent_at TIMESTAMP,

    responded_at TIMESTAMP,


    CONSTRAINT budget_status_check
        CHECK(status IN(
                        'PENDING',
                        'APPROVED',
                        'REJECTED'
            )),


    CONSTRAINT fk_budget_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id)

);