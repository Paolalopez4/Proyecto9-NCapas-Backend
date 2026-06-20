CREATE TABLE notifications (

    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),


    user_id UUID NOT NULL,

    work_order_id UUID,


    title VARCHAR(150),

    body TEXT,


    channel VARCHAR(20) NOT NULL,


    read BOOLEAN DEFAULT FALSE,


    sent_at TIMESTAMP,


    CONSTRAINT notification_channel_check
        CHECK(channel IN(
                         'EMAIL',
                         'SMS',
                         'PUSH'
            )),


    CONSTRAINT fk_notification_user
        FOREIGN KEY(user_id)
            REFERENCES users(id),


    CONSTRAINT fk_notification_work_order
        FOREIGN KEY(work_order_id)
            REFERENCES work_orders(id)

);