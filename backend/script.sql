
CREATE TABLE users (

    id           BIGSERIAL PRIMARY KEY,

    firstname    VARCHAR(255) NOT NULL,

    lastname     VARCHAR(255) NOT NULL,

    email        VARCHAR(320) NOT NULL UNIQUE,

    birthdate    DATE,

    address      TEXT

);



CREATE TABLE agencies (

    id           BIGSERIAL PRIMARY KEY,

    name         VARCHAR(255) NOT NULL,

    city         VARCHAR(255) NOT NULL

);



CREATE TABLE offers (

    id                     BIGSERIAL PRIMARY KEY,

    agency_start_id        BIGINT NOT NULL,

    agency_end_id          BIGINT NOT NULL,

    starts_at              TIMESTAMP NOT NULL,

    ends_at                TIMESTAMP NOT NULL,

    vehicle_category_code  VARCHAR(4) NOT NULL,

    price                  NUMERIC(12,2) NOT NULL,

    currency               VARCHAR(3) NOT NULL,

    CONSTRAINT fk_offers_agency_start

        FOREIGN KEY (agency_start_id) REFERENCES agencies(id),

    CONSTRAINT fk_offers_agency_end

        FOREIGN KEY (agency_end_id)   REFERENCES agencies(id)

);



CREATE TABLE reservations (

    id                    BIGSERIAL PRIMARY KEY,

    user_id               BIGINT NOT NULL,

    offer_id              BIGINT NOT NULL,

    status                VARCHAR(30) NOT NULL,

    created_at            TIMESTAMP NOT NULL,

    CONSTRAINT fk_res_user

        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_res_offer

        FOREIGN KEY (offer_id) REFERENCES offers(id)

);



CREATE TABLE payment_transactions (

    id               BIGSERIAL PRIMARY KEY,

    reservation_id   BIGINT NOT NULL,

    amount           NUMERIC(12,2) NOT NULL,

    status           VARCHAR(30) NOT NULL,

    authorized_at    TIMESTAMP,

    captured_at      TIMESTAMP,

    canceled_at      TIMESTAMP,

    CONSTRAINT fk_pay_reservation

        FOREIGN KEY (reservation_id) REFERENCES reservations(id)

);



CREATE TABLE support_tickets (

    id          BIGSERIAL PRIMARY KEY,

    user_id     BIGINT NOT NULL,

    subject     VARCHAR(255) NOT NULL,

    status      VARCHAR(30) NOT NULL,         -- OPEN / PENDING / RESOLVED / CLOSED

    created_at  TIMESTAMP NOT NULL,

    CONSTRAINT fk_ticket_user

        FOREIGN KEY (user_id) REFERENCES users(id)

);


CREATE TABLE support_messages (

    id           BIGSERIAL PRIMARY KEY,

    ticket_id    BIGINT NOT NULL,

    sender_type  VARCHAR(20) NOT NULL,        -- USER / AGENT / SYSTEM

    channel      VARCHAR(20) NOT NULL,        -- CHAT / EMAIL / VISIO

    body         TEXT NOT NULL,

    created_at   TIMESTAMP NOT NULL,

    CONSTRAINT fk_msg_ticket

        FOREIGN KEY (ticket_id) REFERENCES support_tickets(id) ON DELETE CASCADE

);


