CREATE TABLE branch_inventory (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    branch_id UUID NOT NULL,

    part_id UUID NOT NULL,

    stock INTEGER NOT NULL DEFAULT 0,

    stock_min INTEGER NOT NULL DEFAULT 0,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_inventory_branch
        FOREIGN KEY(branch_id)
            REFERENCES branches(id),


    CONSTRAINT fk_inventory_part
        FOREIGN KEY(part_id)
            REFERENCES parts(id),


    CONSTRAINT unique_branch_part
        UNIQUE(branch_id, part_id)

);