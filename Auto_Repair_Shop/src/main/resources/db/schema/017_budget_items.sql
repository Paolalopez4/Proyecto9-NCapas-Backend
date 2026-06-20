CREATE TABLE budget_items (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    budget_id UUID NOT NULL,


    item_type VARCHAR(20) NOT NULL,

    description TEXT,


    quantity INTEGER DEFAULT 1,


    unit_price NUMERIC(10,2),

    discount NUMERIC(10,2) DEFAULT 0,


    CONSTRAINT item_type_check
        CHECK(item_type IN(
                           'SERVICE',
                           'PART'
            )),


    CONSTRAINT fk_budget_item_budget
        FOREIGN KEY(budget_id)
            REFERENCES budgets(id)

);