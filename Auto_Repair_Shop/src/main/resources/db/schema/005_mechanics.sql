CREATE TABLE mechanics (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    user_id UUID NOT NULL,

    branch_id UUID NOT NULL,

    specialty VARCHAR(150),

    hourly_rate NUMERIC(10,2),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,


    CONSTRAINT fk_mechanic_user
        FOREIGN KEY(user_id)
            REFERENCES users(id),


    CONSTRAINT fk_mechanic_branch
        FOREIGN KEY(branch_id)
            REFERENCES branches(id)

);